package com.example.droneops.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DroneDashboardScreen(viewModel: DroneDashboardViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("DroneOps", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Explicit connection state handling
        val (statusText, statusColor) = when (uiState.connectionState) {
            ConnectionState.Connecting -> "Connecting..." to Color.Gray
            ConnectionState.Connected -> "Connected" to Color.Green
            ConnectionState.Disconnected -> "Disconnected" to Color.Red
        }

        Text(
            text = "Status: $statusText",
            color = statusColor,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Display telemetry if available, even when disconnected
        uiState.telemetry?.let { telemetry ->
            Text("Battery: ${telemetry.batteryPercentage}%")
            Text("Altitude: ${"%.1f".format(telemetry.altitudeMeters)} m")
            Text("Speed: ${"%.1f".format(telemetry.speedMetersPerSecond)} m/s")
            Text("Location: ${"%.4f".format(telemetry.latitude)}, ${"%.4f".format(telemetry.longitude)}")
        } ?: run {
            Text("Awaiting telemetry data...")
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $error", color = Color.Red)
        }
    }
}