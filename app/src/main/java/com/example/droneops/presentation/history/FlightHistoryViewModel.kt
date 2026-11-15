package com.example.droneops.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droneops.data.repository.DroneTelemetryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FlightHistoryViewModel(
    private val repository: DroneTelemetryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlightHistoryUiState>(FlightHistoryUiState.Loading)
    val uiState: StateFlow<FlightHistoryUiState> = _uiState.asStateFlow()

    init {
        loadFlightHistory()
    }

    private fun loadFlightHistory() {
        viewModelScope.launch {
            repository.getFlightHistory()
                .catch {
                    _uiState.value = FlightHistoryUiState.Empty
                }
                .collect { entities ->
                    if (entities.isEmpty()) {
                        _uiState.value = FlightHistoryUiState.Empty
                    } else {
                        val items = entities.map { entity ->
                            FlightHistoryItem(
                                id = entity.id,
                                formattedDate = formatFlightDate(entity.startTimeMillis),
                                formattedDuration = formatDuration(entity.durationSeconds),
                                maxAltitudeMeters = entity.maxAltitudeMeters,
                                averageSpeedMps = entity.averageSpeedMps,
                                minBatteryPercentage = entity.minBatteryPercentage
                            )
                        }
                        _uiState.value = FlightHistoryUiState.Success(items)
                    }
                }
        }
    }

    private fun formatFlightDate(timestampMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestampMillis))
    }

    private fun formatDuration(durationSeconds: Int): String {
        val minutes = durationSeconds / 60
        val seconds = durationSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
