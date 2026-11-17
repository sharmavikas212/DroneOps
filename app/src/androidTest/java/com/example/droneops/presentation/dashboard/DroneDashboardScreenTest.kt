package com.example.droneops.presentation.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.droneops.data.model.DroneTelemetry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DroneDashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dashboard_renders_60_40_split_with_map_and_grid() {
        val testUiState = DroneDashboardUiState(
            connectionState = ConnectionState.Connected,
            telemetry = DroneTelemetry(95, 100.0, 50.0, 10.0, 5.0),
            errorMessage = null
        )

        composeTestRule.setContent {
            StatelessDroneDashboardScreen(
                uiState = testUiState,
                onNavigateToHistory = {}
            )
        }

        // We expect a map container and a telemetry grid
        composeTestRule.onNodeWithTag("map_container").assertIsDisplayed()
        composeTestRule.onNodeWithTag("telemetry_grid").assertIsDisplayed()
    }
}