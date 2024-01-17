package com.example.yummiapp
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class SearchBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    //test search bar for a specific recipe in this case it is pasta
    @Test
    fun testSearchBarForSpecificRecipe() {
        composeTestRule.onNodeWithTag("searchBarField").performTextInput("pasta")
        composeTestRule.onNodeWithTag("searchBarField").performImeAction()

        composeTestRule.waitUntil(
            condition = {
                try {
                    composeTestRule.onNodeWithText("Emerald Pea Pasta").assertExists()
                    true
                } catch (e: AssertionError) {
                    false
                }
            },
            timeoutMillis = 15000 //  waits until the recipe is loaded
        )
    }
}

