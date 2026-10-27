package com.example.droneops.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droneops.data.repository.DroneTelemetryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DroneDashboardViewModel(
    private val repository: DroneTelemetryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DroneDashboardUiState())

    val uiState: StateFlow<DroneDashboardUiState> = _uiState.asStateFlow()

    init {
        observeTelemetry()
    }

    private fun observeTelemetry() {
        viewModelScope.launch {
            repository.observeTelemetry()
                .onEach { telemetry ->
                    _uiState.update {
                        it.copy(
                            telemetry = telemetry,
                            isConnected = true,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isConnected = false,
                            isLoading = false,
                            errorMessage = exception.message
                                ?:"Unable to receive Telemetry"
                        )
                    }
                }
                .collect()
        }
    }
}