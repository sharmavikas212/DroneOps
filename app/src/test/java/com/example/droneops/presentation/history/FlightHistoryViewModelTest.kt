package com.example.droneops.presentation.history

import app.cash.turbine.test
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.FakeTelemetrySource
import com.example.droneops.presentation.dashboard.FakeFlightRecordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlightHistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeSource: FakeTelemetrySource
    private lateinit var fakeDao: FakeFlightRecordDao
    private lateinit var repository: DroneTelemetryRepository
    private lateinit var viewModel: FlightHistoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeSource = FakeTelemetrySource()
        fakeDao = FakeFlightRecordDao()
        repository = DroneTelemetryRepository(fakeSource, fakeDao)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state transitions from Loading to Empty when no records exist`() = runTest {
        viewModel = FlightHistoryViewModel(repository)

        viewModel.uiState.test {
            val item = awaitItem()
            val emptyOrLoading = item
            if (emptyOrLoading is FlightHistoryUiState.Loading) {
                assertEquals(FlightHistoryUiState.Empty, awaitItem())
            } else {
                assertEquals(FlightHistoryUiState.Empty, emptyOrLoading)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when flight records exist, state updates to Success with formatted items`() = runTest {
        val record = com.example.droneops.data.local.FlightRecordEntity(
            id = 1L,
            startTimeMillis = 1700000000000L,
            endTimeMillis = 1700000065000L,
            durationSeconds = 65, // 01:05
            maxAltitudeMeters = 50.0,
            averageSpeedMps = 12.5,
            minBatteryPercentage = 80,
            status = "Completed"
        )
        fakeDao.insertFlightRecord(record)

        viewModel = FlightHistoryViewModel(repository)

        viewModel.uiState.test {
            var currentState = awaitItem()
            while (currentState is FlightHistoryUiState.Loading) {
                currentState = awaitItem()
            }

            val successState = currentState as FlightHistoryUiState.Success
            assertEquals(1, successState.items.size)
            val item = successState.items[0]
            assertEquals(1L, item.id)
            assertEquals("01:05", item.formattedDuration)
            assertEquals("Completed", item.status)
            assertEquals(50.0, item.maxAltitudeMeters, 0.001)
            assertEquals(12.5, item.averageSpeedMps, 0.001)
            assertEquals(80, item.minBatteryPercentage)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
