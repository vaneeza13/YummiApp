package com.example.yummiapp.bottomnavbar

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen() {
    val context = LocalContext.current
    var ingredientText by remember { mutableStateOf("") }
    val allIngredients = remember { mutableStateListOf<Ingredient>() }
    val unCheckedIngredients = allIngredients.filter { !it.isChecked }.toMutableStateList()

    //load  list when screen is displayed
    LaunchedEffect(key1 = true) {
        allIngredients.addAll(loadShoppingList(context))
        unCheckedIngredients.addAll(allIngredients.filter { !it.isChecked })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Shopping List",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFFCAB64),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFF5ED)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFF5ED)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = ingredientText,
                    onValueChange = { ingredientText = it },
                    label = { Text("Add Ingredient") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color(0xFFFCAB64),
                        unfocusedBorderColor = Color(0xFFFCAB64),
                        cursorColor = Color.Black
                    )
                )
                IconButton(
                    onClick = {
                        if (ingredientText.isNotBlank()) {
                            val newIngredient = Ingredient(ingredientText, false)
                            allIngredients.add(newIngredient)
                            unCheckedIngredients.add(newIngredient)
                            ingredientText = ""
                        }
                    }
                ) {
                    Icon(Icons.Filled.Check,
                        contentDescription = "Add",
                        tint = Color(0xFFFCAB64))

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            unCheckedIngredients.forEachIndexed { _, ingredient ->
                IngredientItem(ingredient = ingredient) {
                    val updatedIngredient = ingredient.copy(isChecked = true)
                    allIngredients[allIngredients.indexOf(ingredient)] = updatedIngredient
                    unCheckedIngredients.remove(ingredient)
                }
            }
        }
    }

    //save the full list (including checked items) whenever it changes
    DisposableEffect(key1 = allIngredients) {
        onDispose {
            saveShoppingList(context, allIngredients)
        }
    }
}

@Composable
fun IngredientItem(ingredient: Ingredient, onCheckChanged: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = ingredient.isChecked,
            onCheckedChange = { onCheckChanged() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFFB5A00),
                uncheckedColor = Color.Gray
            )
        )
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp),
            color = if (ingredient.isChecked) Color.Gray else Color.Black
        )
    }
}

data class Ingredient(
    val name: String,
    var isChecked: Boolean
)

fun saveShoppingList(context: Context, ingredients: List<Ingredient>) {
    val sharedPreferences = context.getSharedPreferences("YummiAppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Gson().toJson(ingredients)
    editor.putString("shoppingList", json)
    editor.apply()
}

fun loadShoppingList(context: Context): List<Ingredient> {
    val sharedPreferences = context.getSharedPreferences("YummiAppPreferences", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("shoppingList", null)
    return if (json != null) {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        Gson().fromJson(json, type)
    } else {
        emptyList()
    }
}
