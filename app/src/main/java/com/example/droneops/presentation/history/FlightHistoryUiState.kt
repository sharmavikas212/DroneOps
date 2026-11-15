package com.example.droneops.presentation.history

data class FlightHistoryItem(
    val id: Long,
    val formattedDate: String,
    val formattedDuration: String,
    val maxAltitudeMeters: Double,
    val averageSpeedMps: Double,
    val minBatteryPercentage: Int
)

sealed interface FlightHistoryUiState {
    object Loading : FlightHistoryUiState
    object Empty : FlightHistoryUiState
    data class Success(val items: List<FlightHistoryItem>) : FlightHistoryUiState
}
