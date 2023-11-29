package com.example.yummiapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class CategoryTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSelectCategoryAndCheckForRecipe() {
        // selects the breakfast category
        composeTestRule.onNodeWithTag("breakfastCategory").performClick()

        composeTestRule.waitUntil(
            condition = {
                try {
                    composeTestRule.onNodeWithText("Green Chili And Chorizo Breakfast Strata").assertExists()
                    true
                } catch (e: AssertionError) {
                    false
                }
            },
            timeoutMillis = 15000
        )
    }
}
