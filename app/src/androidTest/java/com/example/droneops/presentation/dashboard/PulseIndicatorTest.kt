package com.example.droneops.presentation.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PulseIndicatorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pulseIndicator_isDisplayed_whenLive() {
        composeTestRule.setContent {
            PulseIndicator(isLive = true)
        }

        composeTestRule.onNodeWithTag("pulse_indicator").assertIsDisplayed()
    }

    @Test
    fun pulseIndicator_isHidden_whenNotLive() {
        composeTestRule.setContent {
            PulseIndicator(isLive = false)
        }

        composeTestRule.onNodeWithTag("pulse_indicator").assertIsNotDisplayed()
    }
}