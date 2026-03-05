package com.example.splitcash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.splitcash.ui.theme.SplitCashTheme
import org.junit.Rule
import org.junit.Test

class TipTimeLayoutTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            SplitCashTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SplitCashLayout()
                }
            }
        }

        // Enter bill amount
        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("10")

        // Enter tip percentage
        composeTestRule.onNodeWithText("Tip %")
            .performTextInput("20")

        // Verify total or other elements if needed
        composeTestRule.onNodeWithText("Total: $12.00", substring = true)
    }
}
