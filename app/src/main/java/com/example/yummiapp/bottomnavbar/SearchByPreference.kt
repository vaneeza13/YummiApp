package com.example.yummiapp.bottomnavbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchByPreferenceScreen(navController: NavHostController) {
    val (selectedIngredients, onIngredientSelected) = remember { mutableStateOf(setOf<String>()) }
    val (selectedServing, onServingSelected) = remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFFFF5ED),
        contentColor = Color(0xFF4A2301)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFB5A00)
                ),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Back", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Search by food preference here",
                color = Color(0xFF4A2301),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Ingredients:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF4A2301)
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val ingredients = listOf("Chicken", "Fish", "Tofu", "Pork", "Seafood", "Fruits", "Beef", "Vegetable", "Egg")
                items(items = ingredients) { ingredient ->
                    CheckboxWithText(
                        text = ingredient,
                        checked = selectedIngredients.contains(ingredient),
                        onCheckedChange = {
                            val newSelection = selectedIngredients.toMutableSet()
                            if (newSelection.contains(ingredient)) {
                                newSelection.remove(ingredient)
                            } else {
                                newSelection.add(ingredient)
                            }
                            onIngredientSelected(newSelection)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Serving amount:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF4A2301)
            )

            val servings = listOf("1", "2", "3", "4", "5", "6+")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(servings) { serving ->
                    RadioButtonWithText(
                        text = serving,
                        selected = selectedServing == serving,
                        onSelect = { onServingSelected(serving) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Implement search functionality */ },
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFB5A00),
                    contentColor = Color.White
                )
            ) {
                Text("Search", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CheckboxWithText(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val checkboxColors = CheckboxDefaults.colors(
        checkedColor = Color(0xFFF9B60B),
        uncheckedColor = Color(0xFFF9B60B),
        checkmarkColor = Color.White
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = checkboxColors,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 8.dp)
                .widthIn(max = 140.dp)
        )
    }
}

@Composable
fun RadioButtonWithText(text: String, selected: Boolean, onSelect: () -> Unit) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color(0xFFF9B60B),
        unselectedColor = Color(0xFFF9B60B)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = radioButtonColors
        )
        Text(
                text = text,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .widthIn(max = 140.dp)
        )
    }
}