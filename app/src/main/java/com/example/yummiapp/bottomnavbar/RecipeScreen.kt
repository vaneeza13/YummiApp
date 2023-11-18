package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Clock
import coil.compose.rememberImagePainter
import com.example.yummiapp.R
import com.example.yummiapp.viewmodels.Recipe
import com.example.yummiapp.viewmodels.RecipeViewModel

@Composable
fun RecipeScreen(recipeViewModel: RecipeViewModel = viewModel()) {
    val recipes = recipeViewModel.recipes.value
    val errorMessage = recipeViewModel.errorMessage.value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Recipes", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Try our delicious recommended recipes here or back to search for more preferences :-)",
            style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        DisplayRecipeData(recipes, errorMessage)
    }
}

@Composable
fun DisplayRecipeData(recipes: List<Recipe>?, errorMessage: String?) {
    if (errorMessage != null) {
        Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge)
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(recipes.orEmpty()) { recipe ->
                RecipeCard(recipe)
            }

            if (recipes.isNullOrEmpty()) {
                item { Text(text = "Loading...") }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.spaghetti2), // Local resource image
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconAndText(text = recipe.title)
                IconAndText(text = recipe.servings)
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text("More..", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun IconAndText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
