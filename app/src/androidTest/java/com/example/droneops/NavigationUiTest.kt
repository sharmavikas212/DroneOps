package com.example.droneops

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDashboardToHistoryNavigationAndBack() {
        // 1. Verify we are on Dashboard
        composeTestRule.onNodeWithText("DroneOps").assertExists()
        composeTestRule.onNodeWithText("View Flight History").assertExists()

        // 2. Click "View Flight History" button
        composeTestRule.onNodeWithText("View Flight History").performClick()

        // 3. Verify we navigated to History screen
        composeTestRule.onNodeWithText("Flight History").assertExists()

        // 4. Click back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // 5. Verify we are back on Dashboard
        composeTestRule.onNodeWithText("View Flight History").assertExists()
    }
}
