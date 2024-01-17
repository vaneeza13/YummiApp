package com.example.yummiapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.yummiapp.model.Recipe
import com.example.yummiapp.viewmodel.RecipeViewModel

//composable for when browsing through recipes
@Composable
fun RecipeScreen(
    recipeViewModel: RecipeViewModel,
    navController: NavHostController,
    query: String?
) {
    // searches for recipes when the query is changed
    LaunchedEffect(query) {
        query?.let {
            if (it.isNotBlank()) {
                recipeViewModel.searchRecipes(it)
            }
        }
    }

    val recipes = recipeViewModel.recipes.value
    val errorMessage = recipeViewModel.errorMessage.value

    // displaying data  from query
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5ED))
    ) {
        Text(
            text = if (!query.isNullOrBlank()) "Search result for \"$query\"" else "Recipes",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFFFCAB64)
        )

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 3.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFB5A00)
            )
        ) {
            Text("Back", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // check error message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .testTag("searchResultsContainer") // Place it here
                    .fillMaxSize()
            ) {
                items(recipes.orEmpty()) { recipe ->
                    RecipeCard(recipe, navController);
                }
            }
        }
    }
}

// this is the card for each recipe there is
@Composable
fun RecipeCard(recipe: Recipe, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipe.imageUrl),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconAndText(text = recipe.servings)
                Button(
                    onClick = {
                        navController.navigate("RecipeDetails/${recipe.id}")
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCAB64)
                    )
                ) {
                    Text("More..", color = Color.White)
                }
            }
        }
    }
}

// icon and text for each of the recipes
@Composable
fun IconAndText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
