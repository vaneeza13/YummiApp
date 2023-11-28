Feature: Select Recipe Category
  As a young man
  I want to be able to select a category for a meal type
  So that I can get ideas for that type of meal

  Scenario: Selecting a breakfast category
    Given I am on the home screen
    When I select the "Breakfast" category
    Then I should see a list of breakfast recipes
