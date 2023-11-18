package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yummiapp.viewmodels.Recipe
import com.example.yummiapp.viewmodels.RecipeViewModel


@Composable
fun RecipeScreen(recipeViewModel: RecipeViewModel = viewModel()) {
    val recipes = recipeViewModel.recipes.value
    val errorMessage = recipeViewModel.errorMessage.value

    DisplayRecipeData(recipes, errorMessage)
}

@Composable
fun DisplayRecipeData(recipes: List<Recipe>?, errorMessage: String?) {
    if (errorMessage != null) {
        Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge)
    } else {
        // Use LazyColumn for a scrollable list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(recipes.orEmpty().take(5)) { recipe ->
                Text(text = recipe.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Ingredients: " + recipe.ingredients, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Instructions: " + recipe.instructions, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recipe.servings, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (recipes.isNullOrEmpty()) {
                item { Text(text = "Loading...") }
            }
        }
    }
}