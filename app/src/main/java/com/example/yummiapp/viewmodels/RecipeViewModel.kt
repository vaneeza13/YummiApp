package com.example.yummiapp.viewmodels


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeViewModel : ViewModel() {
    // Private mutable state
    private val _recipes = mutableStateOf<List<Recipe>?>(null)
    private val _errorMessage = mutableStateOf<String?>(null)

    // Exposed state
    val recipes: State<List<Recipe>?> = _recipes
    val errorMessage: State<String?> = _errorMessage

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                val response = makeAPICall()
                val data = response.body?.string()

                if (data != null) {
                    val type = object : TypeToken<List<Recipe>>() {}.type
                    _recipes.value = Gson().fromJson(data, type)
                } else {
                    _errorMessage.value = "Failed to fetch data"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    private suspend fun makeAPICall(): Response {
        val client = OkHttpClient()
        val myDish = "Sushi"

        val request = Request.Builder()
            .url("https://recipe-by-api-ninjas.p.rapidapi.com/v1/recipe?query=$myDish")
            .get()
            .addHeader("X-RapidAPI-Key", "dea0d1e227mshdef66c06d9d9811p19a79ajsnd0169e064d08")
            .addHeader("X-RapidAPI-Host", "recipe-by-api-ninjas.p.rapidapi.com")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }
}

// Recipe Model
data class Recipe(val title: String, val ingredients: String, val servings: String, val instructions: String)