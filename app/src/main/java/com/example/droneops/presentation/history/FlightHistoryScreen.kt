package com.example.droneops.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightHistoryScreen(
    viewModel: FlightHistoryViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flight History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is FlightHistoryUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is FlightHistoryUiState.Empty -> {
                    Text("No flight history recorded yet.", style = MaterialTheme.typography.bodyLarge)
                }
                is FlightHistoryUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.items) { item ->
                            FlightHistoryCard(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightHistoryCard(item: FlightHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Date: ${item.formattedDate}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Duration: ${item.formattedDuration}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Max Altitude: ${"%.1f".format(item.maxAltitudeMeters)} m", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Avg Speed: ${"%.1f".format(item.averageSpeedMps)} m/s", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Min Battery: ${item.minBatteryPercentage}%", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
