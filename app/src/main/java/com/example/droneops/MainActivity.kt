package com.example.droneops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.SimulatedTelemetrySource
import com.example.droneops.presentation.dashboard.DroneDashboardScreen
import com.example.droneops.presentation.dashboard.DroneDashboardViewModel
import com.example.droneops.presentation.dashboard.DroneDashboardViewModelFactory
import com.example.droneops.ui.theme.DroneOpsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val source = SimulatedTelemetrySource()
        val repository = DroneTelemetryRepository(source)

        enableEdgeToEdge()

        val factory = DroneDashboardViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory)
            .get(DroneDashboardViewModel::class.java)

        setContent {
            DroneOpsTheme {
                DroneDashboardScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DroneOpsTheme {
        Greeting("Android")
    }
}