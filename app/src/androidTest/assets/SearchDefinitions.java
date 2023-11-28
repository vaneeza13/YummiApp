
import androidx.compose.ui.test.junit4.createAndroidComposeRule;
import androidx.compose.ui.test.onNodeWithText;
import androidx.compose.ui.test.performClick;
import androidx.compose.ui.test.performTextInput;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Rule;

public class SearchStepDefinitions {

    @Rule
    public final createAndroidComposeRule<MainActivity> composeTestRule = createAndroidComposeRule<>();

    @Given("^I am on the home screen$")
    public void i_am_on_the_home_screen() {
        composeTestRule.onNodeWithText("Yummi").assertExists();
    }

    @When("^I enter \"([^\"]*)\" into the search bar$")
    public void i_enter_into_the_search_bar(String searchQuery) {
        composeTestRule.onNodeWithText("Search").performClick();
        composeTestRule.onNodeWithText("Search").performTextInput(searchQuery);
    }

    @Then("^I should see a list of recipes containing \"([^\"]*)\"$")
    public void i_should_see_a_list_of_recipes_containing(String recipe) {
        composeTestRule.onNodeWithText(recipe).assertExists();
    }
}
