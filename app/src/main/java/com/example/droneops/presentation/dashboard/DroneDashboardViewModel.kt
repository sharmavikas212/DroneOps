package com.example.droneops.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droneops.data.repository.DroneTelemetryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class DroneDashboardViewModel(
    private val repository: DroneTelemetryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DroneDashboardUiState())

    val uiState: StateFlow<DroneDashboardUiState> = _uiState.asStateFlow()

    // 1. Declare a Job to hold the countdown timer
    private var watchdogJob: Job? = null

    init {
        observeTelemetry()
    }

    private fun observeTelemetry() {
        resetWatchdog() // Edge Case 1: Start timer for the initial connection

        viewModelScope.launch {
            repository.observeTelemetry()
                .onEach { telemetry ->
                    _uiState.update {
                        it.copy(
                            telemetry = telemetry,
                            connectionState = ConnectionState.Connected,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    // Reset the 3-second timer on every successful reading
                    resetWatchdog()
                }
                .onCompletion { // Edge Case 2: Handle graceful stream end
                    _uiState.update {
                        it.copy(connectionState = ConnectionState.Disconnected)
                    }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            connectionState = ConnectionState.Disconnected,
                            isLoading = false,
                            errorMessage = exception.message
                                ?:"Unable to receive Telemetry"
                        )
                    }
                }
                .collect()
        }
    }

    // The watchdog logic
    private fun resetWatchdog() {
        watchdogJob?.cancel() // Cancel the previous timer
        watchdogJob = viewModelScope.launch {
            delay(3_000.milliseconds) // Wait 3 seconds
            // If we reach this line, the timer wasn't cancelled by a new telemetry reading
            _uiState.update {
                it.copy(connectionState = ConnectionState.Disconnected)
            }
        }
    }
}