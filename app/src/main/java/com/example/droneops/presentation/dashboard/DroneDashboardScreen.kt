package com.example.droneops.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.droneops.data.model.DroneTelemetry
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun DroneDashboardScreen(
    viewModel: DroneDashboardViewModel,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatelessDroneDashboardScreen(
        uiState = uiState,
        onNavigateToHistory = onNavigateToHistory
    )
}

@Composable
fun StatelessDroneDashboardScreen(
    uiState: DroneDashboardUiState,
    onNavigateToHistory: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("DroneOps", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Explicit connection state handling
        val (statusText, statusColor) = when (uiState.connectionState) {
            ConnectionState.Connecting -> "Connecting..." to Color.Gray
            ConnectionState.Connected -> "Connected" to Color.Green
            ConnectionState.Disconnected -> "Disconnected" to Color.Red
        }
        
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(
                text = "Status: $statusText",
                color = statusColor,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            PulseIndicator(isLive = uiState.connectionState == ConnectionState.Connected)
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(0.6f).fillMaxWidth().testTag("map_container")) {
            val cameraPositionState = rememberCameraPositionState()
            
            LaunchedEffect(uiState.telemetry) {
                uiState.telemetry?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude), 15f
                    )
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Polyline(
                    points = uiState.trace,
                    color = Color.Red,
                    width = 8f
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(0.4f).fillMaxWidth()) {
            uiState.telemetry?.let { telemetry ->
                TelemetryGrid(telemetry = telemetry, modifier = Modifier.testTag("telemetry_grid"))
            } ?: run {
                Text("Awaiting telemetry data...", modifier = Modifier.testTag("telemetry_grid"))
            }
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Error: $error", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigateToHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Flight History")
        }
    }
}

@Composable
fun TelemetryGrid(telemetry: DroneTelemetry, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TelemetryTile(
                label = "Altitude",
                value = "${"%.1f".format(telemetry.altitudeMeters)} m",
                modifier = Modifier.weight(1f)
            )
            TelemetryTile(
                label = "Speed",
                value = "${"%.1f".format(telemetry.speedMetersPerSecond)} m/s",
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TelemetryTile(
                label = "Battery",
                value = "${telemetry.batteryPercentage}%",
                modifier = Modifier.weight(1f)
            )
            TelemetryTile(
                label = "Location",
                value = "${"%.4f".format(telemetry.latitude)}, ${"%.4f".format(telemetry.longitude)}",
                isSecondary = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TelemetryTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    Box(
        modifier = modifier
            .background(backgroundColor, androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                style = if (isSecondary) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = if (isSecondary) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PulseIndicator(isLive: Boolean, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    if (isLive) {
        Box(
            modifier = modifier
                .size(12.dp)
                .alpha(alpha)
                .background(Color.Green, CircleShape)
                .testTag("pulse_indicator")
        )
    } else {
        // We still render a box so it takes up space, but it's invisible and untaggable
        // Actually, the test requires it to NOT be displayed.
         Box(
            modifier = modifier
                .size(12.dp)
                .alpha(0f)
        )
    }
}
