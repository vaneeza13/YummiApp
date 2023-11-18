package com.example.yummiapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yummiapp.bottomnavbar.FavoriteScreen
import com.example.yummiapp.bottomnavbar.HomeScreen
import com.example.yummiapp.bottomnavbar.RecipeScreen
import com.example.yummiapp.bottomnavbar.ShoppingScreen
import com.example.yummiapp.ui.theme.YummiAppTheme
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart

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
        "Recipes",
        "Favorites",
        "Shopping"
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(items, navController) }
    ) {
        NavHost(navController, startDestination = "Home") {
            composable("Home") { HomeScreen() }
            composable("Recipes") { RecipeScreen() }
            composable("Favorites") { FavoriteScreen() }
            composable("Shopping") { ShoppingScreen() }
        }
    }
}


@Composable
fun BottomNavigationBar(items: List<String>, navController: NavHostController) {

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
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
                        else -> Icon(Icons.Default.Favorite, contentDescription = null) // Default or fallback icon
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