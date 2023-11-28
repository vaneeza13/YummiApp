Feature: Recipe Search
  As a Mom of three
  I want to search for recipes
  So that my kids can have a delicious meal

  Scenario: Searching for a specific recipe
    Given I am on the home screen
    When I enter "pizza" into the search bar
    Then I should see a list of recipes containing "pizza"