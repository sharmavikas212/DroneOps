package com.example.droneops.presentation.dashboard

import com.example.droneops.data.model.DroneTelemetry

enum class ConnectionState {
    Connecting,
    Connected,
    Disconnected
}
data class DroneDashboardUiState(
    val telemetry: DroneTelemetry? = null,
    val connectionState: ConnectionState = ConnectionState.Connecting,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
