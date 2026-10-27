package com.example.droneops.presentation.dashboard

import com.example.droneops.data.model.DroneTelemetry

data class DroneDashboardUiState(
    val telemetry: DroneTelemetry? = null,
    val isConnected: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
