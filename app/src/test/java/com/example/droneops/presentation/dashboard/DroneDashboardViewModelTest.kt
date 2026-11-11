package com.example.droneops.presentation.dashboard

import app.cash.turbine.test
import com.example.droneops.data.model.DroneTelemetry
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.FakeTelemetrySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class DroneDashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeSource: FakeTelemetrySource
    private lateinit var repository: DroneTelemetryRepository
    private lateinit var viewModel: DroneDashboardViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        fakeSource = FakeTelemetrySource()
        repository = DroneTelemetryRepository(fakeSource)
        viewModel = DroneDashboardViewModel(repository)
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Connecting and telemetry is null`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(ConnectionState.Connecting, state.connectionState)
            assertEquals(null, state.telemetry)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when telemetry is emitted, state updates to Connected`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial state

            // Manually emit fake data
            val testTelemetry = DroneTelemetry(100, 10.0, 5.0, -33.0, 151.0)
            fakeSource.emitTelemetry(testTelemetry)

            val state = awaitItem()

            assertEquals(ConnectionState.Connected, state.connectionState)
            assertEquals(100, state.telemetry?.batteryPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when no telemetry received for 3 seconds, state becomes Disconnected but retains data`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial state

            val testTelemetry = DroneTelemetry(95, 20.0, 10.0, -33.0, 151.0)
            fakeSource.emitTelemetry(testTelemetry)

            awaitItem() // Skip the Connected state we just triggered

            // Fast-forward virtual time past the ViewModel's 3-second watchdog timer
            advanceTimeBy(3001.milliseconds)

            val state = awaitItem()

            // Verify watchdog tripped
            assertEquals(ConnectionState.Disconnected, state.connectionState)
            // Verify stale data is kept on screen
            assertEquals(95, state.telemetry?.batteryPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }
}