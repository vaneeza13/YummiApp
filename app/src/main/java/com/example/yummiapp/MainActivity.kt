package com.example.yummiapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummiapp.bottomnavbar.FavoriteScreen
import com.example.yummiapp.bottomnavbar.HomeScreen
import com.example.yummiapp.bottomnavbar.RecipeScreen
import com.example.yummiapp.bottomnavbar.ShoppingScreen
import com.example.yummiapp.ui.theme.YummiAppTheme
import com.example.yummiapp.viewmodels.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummiAppTheme {
                MainScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        "Home",
        "Favorites",
        "Shopping"
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(items, navController) }
    ) {
        NavHost(navController, startDestination = "Home") {
            composable("Home") { HomeScreen(navController) }
            composable("Favorites") { FavoriteScreen() }
            composable("Shopping") { ShoppingScreen() }
            composable("Recipes/{query}", arguments = listOf(navArgument("query") { type = NavType.StringType })) { backStackEntry ->
                val recipeViewModel: RecipeViewModel = viewModel()
                RecipeScreen(recipeViewModel, navController, backStackEntry.arguments?.getString("query"))
            }
        }
    }
}

@Composable
fun BottomNavigationBar(items: List<String>, navController: NavHostController) {

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
                        "Recipes" -> Icon(Icons.Default.List, contentDescription = null)
                        "Favorites" -> Icon(Icons.Default.Favorite, contentDescription = null)
                        "Shopping" -> Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        else -> Icon(Icons.Default.Favorite, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
