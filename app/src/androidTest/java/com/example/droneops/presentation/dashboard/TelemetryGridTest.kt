package com.example.droneops.presentation.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.droneops.data.model.DroneTelemetry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TelemetryGridTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun telemetryGrid_displaysPrimaryMetrics() {
        val telemetry = DroneTelemetry(100, 10.0, 5.0, 50.0, 50.0)

        composeTestRule.setContent {
            TelemetryGrid(telemetry = telemetry)
        }

        // Wait for idle before assertions
        composeTestRule.waitForIdle()

        // Verify primary metrics are displayed with units
        composeTestRule.onNodeWithText("Altitude: 10.0 m").assertIsDisplayed() // Altitude
        composeTestRule.onNodeWithText("Speed: 5.0 m/s").assertIsDisplayed() // Speed
        composeTestRule.onNodeWithText("Battery: 100%").assertIsDisplayed() // Battery
    }
}