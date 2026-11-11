package com.example.droneops.presentation.dashboard

import app.cash.turbine.test
import com.example.droneops.data.local.FlightRecordDao
import com.example.droneops.data.local.FlightRecordEntity
import com.example.droneops.data.model.DroneTelemetry
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.FakeTelemetrySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

/**
 * Unit tests for [DroneDashboardViewModel].
 * 
 * These tests verify:
 * 1. The initial connection state of the dashboard.
 * 2. State transitions when telemetry data is received (Connecting -> Connected).
 * 3. The 3-second watchdog timer mechanism (transitioning to Disconnected while retaining last known telemetry).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DroneDashboardViewModelTest {

    // Controls virtual time and coroutine execution order in tests
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeSource: FakeTelemetrySource
    private lateinit var fakeDao: FakeFlightRecordDao
    private lateinit var repository: DroneTelemetryRepository
    private lateinit var viewModel: DroneDashboardViewModel

    @Before
    fun setup() {
        // Redirect main dispatcher to our test dispatcher for predictable coroutine testing
        Dispatchers.setMain(testDispatcher)

        // Initialize fake data sources and ViewModel before each test
        fakeSource = FakeTelemetrySource()
        fakeDao = FakeFlightRecordDao()
        repository = DroneTelemetryRepository(fakeSource, fakeDao)
        viewModel = DroneDashboardViewModel(repository)
    }

    @After
    fun teardown() {
        // Reset main dispatcher after tests complete
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Connecting and telemetry is null`() = runTest {
        // Use Turbine (.test) to observe StateFlow emissions
        viewModel.uiState.test {
            val state = awaitItem()

            // Verify default state when app/dashboard first loads
            assertEquals(ConnectionState.Connecting, state.connectionState)
            assertEquals(null, state.telemetry)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when telemetry is emitted, state updates to Connected`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial "Connecting" state

            // Manually emit fake telemetry data into our test source
            val testTelemetry = DroneTelemetry(100, 10.0, 5.0, -33.0, 151.0)
            fakeSource.emitTelemetry(testTelemetry)

            val state = awaitItem()

            // Verify state changes to Connected and battery data is present
            assertEquals(ConnectionState.Connected, state.connectionState)
            assertEquals(100, state.telemetry?.batteryPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when no telemetry received for 3 seconds, state becomes Disconnected but retains data`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Skip initial state

            // Send initial telemetry so connection is established
            val testTelemetry = DroneTelemetry(95, 20.0, 10.0, -33.0, 151.0)
            fakeSource.emitTelemetry(testTelemetry)

            awaitItem() // Skip the Connected state

            // Fast-forward virtual time past the ViewModel's 3-second watchdog timer
            advanceTimeBy(3001.milliseconds)

            val state = awaitItem()

            // Verify watchdog tripped (state becomes Disconnected)
            assertEquals(ConnectionState.Disconnected, state.connectionState)
            // Verify stale/last known telemetry data is kept on screen
            assertEquals(95, state.telemetry?.batteryPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

/**
 * A lightweight in-memory fake DAO used for testing purposes.
 * This replaces the real Room database DAO so unit tests can run instantly 
 * on the local JVM without needing an Android context or SQLite setup.
 */
class FakeFlightRecordDao : FlightRecordDao {

    // MutableStateFlow holds our in-memory "database" table and automatically emits updates
    private val recordsFlow = MutableStateFlow<List<FlightRecordEntity>>(emptyList())
    private var nextId = 1L

    override suspend fun insertFlightRecord(record: FlightRecordEntity) {
        // Simulate Room's auto-generating ID behavior if id is 0
        val recordWithId = if (record.id == 0L) record.copy(id = nextId++) else record

        recordsFlow.update { currentRecords ->
            // Add the new record and match the DAO's "ORDER BY startTimeMillis DESC" behavior
            (currentRecords + recordWithId).sortedByDescending { it.startTimeMillis }
        }
    }

    override fun getAllFlightRecords(): Flow<List<FlightRecordEntity>> {
        return recordsFlow.asStateFlow()
    }
}
