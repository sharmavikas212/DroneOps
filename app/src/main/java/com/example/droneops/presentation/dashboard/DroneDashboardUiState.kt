package com.example.droneops.presentation.dashboard

import com.example.droneops.data.model.DroneTelemetry

import com.google.android.gms.maps.model.LatLng

enum class ConnectionState {
    Connecting,
    Connected,
    Disconnected
}
data class DroneDashboardUiState(
    val telemetry: DroneTelemetry? = null,
    val connectionState: ConnectionState = ConnectionState.Connecting,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val trace: List<LatLng> = emptyList()
)
