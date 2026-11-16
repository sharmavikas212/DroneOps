package com.example.droneops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.droneops.data.local.DroneDatabase
import com.example.droneops.data.repository.DroneTelemetryRepository
import com.example.droneops.data.source.SimulatedTelemetrySource
import com.example.droneops.presentation.dashboard.DroneDashboardScreen
import com.example.droneops.presentation.dashboard.DroneDashboardViewModel
import com.example.droneops.presentation.dashboard.DroneDashboardViewModelFactory
import com.example.droneops.presentation.history.FlightHistoryScreen
import com.example.droneops.presentation.history.FlightHistoryViewModel
import com.example.droneops.presentation.history.FlightHistoryViewModelFactory
import com.example.droneops.ui.theme.DroneOpsTheme

object NavRoutes {
    const val DASHBOARD = "dashboard"
    const val HISTORY = "history"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DroneOpsTheme {
                val navController = rememberNavController()
                val context = applicationContext

                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.DASHBOARD,
                    enterTransition = {
                        fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f, animationSpec = tween(300))
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 1.1f, animationSpec = tween(300))
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 1.1f, animationSpec = tween(300))
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.9f, animationSpec = tween(300))
                    }
                ) {
                    composable(NavRoutes.DASHBOARD) {
                        val dashboardFactory = DroneDashboardViewModelFactory(context)
                        val dashboardViewModel = ViewModelProvider(this@MainActivity, dashboardFactory)
                            .get(DroneDashboardViewModel::class.java)

                        DroneDashboardScreen(
                            viewModel = dashboardViewModel,
                            onNavigateToHistory = {
                                navController.navigate(NavRoutes.HISTORY)
                            }
                        )
                    }
                    composable(NavRoutes.HISTORY) {
                        val database = DroneDatabase.getDatabase(context)
                        val repository = DroneTelemetryRepository(
                            SimulatedTelemetrySource(),
                            database.flightRecordDao()
                        )
                        val historyFactory = FlightHistoryViewModelFactory(repository)
                        val historyViewModel = ViewModelProvider(this@MainActivity, historyFactory)
                            .get(FlightHistoryViewModel::class.java)

                        FlightHistoryScreen(
                            viewModel = historyViewModel,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
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
