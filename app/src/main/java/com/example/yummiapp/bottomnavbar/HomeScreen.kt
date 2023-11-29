package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.yummiapp.R
import com.example.yummiapp.viewmodels.RecipeViewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, recipeViewModel: RecipeViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Yummi",
                        color = Color(0xFFA96C36),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFF5ED)
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFF5ED)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureRecipeSection()
            SearchBar(searchText, onSearchChanged = { searchText = it }) {
                if (searchText.isNotBlank()) {
                    navController.navigate("Recipes/$searchText")
                }
            }
            CategorySection(onCategoryClick = { category ->
                navController.navigate("Recipes/$category")
            })
            SeasonalSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchChanged,
        modifier = Modifier
            .testTag("searchBarField")
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
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch.invoke() })
    )
}

@Composable
fun FeatureRecipeSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF5ED)),
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

@Composable
fun CategorySection(onCategoryClick: (String) -> Unit) {
    Text(
        text = "Choose by recipe’s category:",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color(0xFF4A2301),
        modifier = Modifier.padding(horizontal = 25.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.weight(1f))
        CategoryItem("Breakfast", painterResource(id = R.drawable.breakfast)) {
            onCategoryClick("Breakfast")
        }
        Spacer(Modifier.weight(1f))
        CategoryItem("Lunch", painterResource(id = R.drawable.lunch)) {
            onCategoryClick("Lunch")
        }
        Spacer(Modifier.weight(1f))

        CategoryItem("Dinner", painterResource(id = R.drawable.dinner)) {
            onCategoryClick("Dinner")
        }
        Spacer(Modifier.weight(1f))

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.weight(1f))

        CategoryItem("Dessert", painterResource(id = R.drawable.dessert)) {
            onCategoryClick("Dessert")
        }
        Spacer(Modifier.weight(1f))

        CategoryItem("Snack", painterResource(id = R.drawable.snack)) {
            onCategoryClick("Snack")
        }
        Spacer(Modifier.weight(1f))

        CategoryItem("Drinks", painterResource(id = R.drawable.drink)) {
            onCategoryClick("Drinks")
        }
        Spacer(Modifier.weight(1f))

    }
}
@Composable
fun CategoryItem(name: String, icon: Painter, onClick: () -> Unit) {
    Image(
        painter = icon,
        contentDescription = name,
        modifier = Modifier
            .size(110.dp)
            .clip(RectangleShape)
            .clickable { onClick() }
            .testTag("${name.lowercase(Locale.ROOT)}Category") // TEST TAG

    )
}
@Composable
fun SeasonalSection() {
    Text(
        text = "Seasonal ingredients:",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color(0xFF4A2301),
        modifier = Modifier.padding(horizontal = 25.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SeasonalItem("Pumpkins", painterResource(id = R.drawable.pumpkins))
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

