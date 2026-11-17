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
import com.google.android.gms.maps.model.LatLng

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
                        // Check if the drone has landed or battery is dead
                        val isLandedOrDead = telemetry.batteryPercentage <= 0 || (telemetry.altitudeMeters <= 0.0 && telemetry.speedMetersPerSecond <= 0.0)
                        
                        it.copy(
                            telemetry = telemetry,
                            connectionState = ConnectionState.Connected,
                            isLoading = false,
                            errorMessage = null,
                            trace = if (isLandedOrDead) emptyList() else it.trace + LatLng(telemetry.latitude, telemetry.longitude)
                        )
                    }
                    // Reset the 3-second timer on every successful reading
                    resetWatchdog()
                }
                .onCompletion { // Edge Case 2: Handle graceful stream end
                    _uiState.update {
                        it.copy(connectionState = ConnectionState.Disconnected, trace = emptyList())
                    }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            connectionState = ConnectionState.Disconnected,
                            isLoading = false,
                            errorMessage = exception.message
                                ?:"Unable to receive Telemetry"
                            // Trace is not cleared on random errors either
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
                it.copy(connectionState = ConnectionState.Disconnected) // Do not clear the trace on temporary disconnect
            }
        }
    }
}