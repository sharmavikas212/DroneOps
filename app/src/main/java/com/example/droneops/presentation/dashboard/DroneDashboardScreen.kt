package com.example.droneops.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DroneDashboardScreen(
    viewModel: DroneDashboardViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "DroneOps",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Drone Dashboard",
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider()

        Text(
            text = "Connection: ${
                if (uiState.isConnected) "Connected" else "Disconnected"
            }",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Battery: ${
                uiState.telemetry?.batteryPercentage ?: "--"
            }%",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Altitude: ${
                uiState.telemetry?.altitudeMeters ?: "--"
            } m"
        )

        Text(
            text = "Speed: ${
                uiState.telemetry?.speedMetersPerSecond ?: "--"
            } m/s"
        )
    }
}