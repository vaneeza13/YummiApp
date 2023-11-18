package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yummiapp.R
import com.example.yummiapp.viewmodels.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(recipeViewModel: RecipeViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("Yummi", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureRecipeSection()
            SearchBar(searchText) { searchText = it }
            CategorySection()
            SeasonalSection()
        }
    }
}

@Composable
fun FeatureRecipeSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.onSecondary
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Image(
                painter = painterResource(id = R.drawable.spaghetti),
                contentDescription = "Featured Recipe",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "#1 New recipe of the day!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchChanged: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchChanged,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        placeholder = { Text(text = "Search for delicious recipes") },
        leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null) },
        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            placeholderColor = Color.Gray,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun CategorySection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Choose by recipe's category:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem("Breakfast", painterResource(id = R.drawable.breakfast))
            CategoryItem("Lunch", painterResource(id = R.drawable.lunch))
            CategoryItem("Dinner", painterResource(id = R.drawable.dinner))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem("Dessert", painterResource(id = R.drawable.dessert))
            CategoryItem("Snack", painterResource(id = R.drawable.snack))
            CategoryItem("Drinks", painterResource(id = R.drawable.drink))
        }
    }
}

@Composable
fun CategoryItem(name: String, icon: Painter) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = icon,
            contentDescription = name,
            modifier = Modifier
                .size(88.dp)
                .clip(RectangleShape)
        )
        Text(text = name, color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
}

@Composable
fun SeasonalSection() {
    Text(
        text = "Seasonal ingredients:",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SeasonalItem("Pumpkin", painterResource(id = R.drawable.pumpkins))
        SeasonalItem("Apples", painterResource(id = R.drawable.apples))
        SeasonalItem("Mushrooms", painterResource(id = R.drawable.mushrooms))
    }
}

@Composable
fun SeasonalItem(name: String, image: Painter) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = image,
            contentDescription = name,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(text = name)
    }
}
