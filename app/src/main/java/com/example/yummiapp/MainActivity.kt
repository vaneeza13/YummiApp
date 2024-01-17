package com.example.yummiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yummiapp.ui.theme.YummiAppTheme
import com.example.yummiapp.view.FavoriteScreen
import com.example.yummiapp.view.HomeScreen
import com.example.yummiapp.view.RecipeDetails
import com.example.yummiapp.view.RecipeScreen
import com.example.yummiapp.view.SearchByPreferenceScreen
import com.example.yummiapp.view.ShoppingScreen
import com.example.yummiapp.viewmodel.RecipeViewModel


//entry point of the app and sets up ViewModel
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummiAppTheme {
                //using the factory to create the ViewModel
                val recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModel.RecipeViewModelFactory(
                    applicationContext
                )
                )
                MainScreenContent(recipeViewModel)
            }
        }
    }
    
// display main content
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(recipeViewModel: RecipeViewModel) {
    //use the passed viewModel instead of creating a new one
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHostContainer(navController, recipeViewModel, innerPadding)
    }
}
// this is the bar that is at the bottom of the screen
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("Home", "Favorites", "Shopping")
    BottomNavigation(
        backgroundColor = Color(0xFFFCD4B0),
        contentColor = Color(0xFFFB5A00)
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item,
                onClick = { navController.navigate(item) },
                label = { Text(item) },
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Default.Home, contentDescription = null)
                        "Favorites" -> Icon(Icons.Default.Favorite, contentDescription = null)
                        "Shopping" -> Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        else -> Icon(Icons.Default.Home, contentDescription = null) // Fallback icon
                    }
                }
            )
        }
    }
}
// returns current route
@Composable
fun currentRoute(navController: NavHostController): String? {
    val navStackEntry = navController.currentBackStackEntryAsState()
    return navStackEntry.value?.destination?.route
}

// container for the navigation
@Composable
fun NavHostContainer(navController: NavHostController, recipeViewModel: RecipeViewModel, innerPadding: PaddingValues) {
    NavHost(navController, startDestination = "Home", modifier = Modifier.padding(innerPadding)) {
        composable("Home") { HomeScreen(navController, recipeViewModel) }
        composable("Favorites") {
            FavoriteScreen(viewModel = recipeViewModel, navController = navController)
        }
        composable("Recipes/{query}") { backStackEntry ->
            val searchQuery = backStackEntry.arguments?.getString("query") ?: ""
            RecipeScreen(recipeViewModel, navController, searchQuery)
        }
        composable("RecipeDetails/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            val recipe = recipeViewModel.findRecipeById(recipeId)
            recipe?.let {
                RecipeDetails(it, navController, viewModel = recipeViewModel)
            }
        }
        composable("SearchByPreference") { SearchByPreferenceScreen(navController, viewModel = recipeViewModel) }
        composable("Shopping") { ShoppingScreen() } // Added this line for ShoppingScreen

    }

}
}
