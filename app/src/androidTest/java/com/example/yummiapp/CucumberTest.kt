package com.example.yummiapp

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(

    features = ["app/src/androidTest/assets/features/search_recipe.feature"],
    glue = ["app/src/androidTest/assets/SearchDefinitions.java"],
    plugin = ["pretty"]
)
class CucumberTest
