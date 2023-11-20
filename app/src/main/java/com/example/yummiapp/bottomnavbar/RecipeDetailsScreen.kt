package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yummiapp.viewmodels.Recipe

@Composable
fun RecipeDetailsScreen(recipe: Recipe) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = recipe.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Servings: ${recipe.servings}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Ingredients: ${recipe.ingredients}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Instructions: ${recipe.instructions}", style = MaterialTheme.typography.bodySmall)
    }
}
