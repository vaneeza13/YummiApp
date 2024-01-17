package com.example.yummiapp.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yummiapp.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.UUID

//viewModel class for managing recipe related data and operations
class RecipeViewModel(private val context: Context) : ViewModel() {
    //shared preferences for storing and getting local data
    private val sharedPreferences = context.getSharedPreferences("YummiAppPreferences", Context.MODE_PRIVATE)
    //for serialization and deserialization
    private val gson = Gson()

    //state for recipes, error messages, navigation triggers, and favorite recipes
    private val myRecipes = mutableStateOf<List<Recipe>?>(null)
    val recipes: State<List<Recipe>?> = myRecipes

    private val myErrorMessages = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = myErrorMessages

    private val _navigateToRecipes = mutableStateOf<String?>(null)
    val navigateToRecipes: State<String?> = _navigateToRecipes

    private val _favoriteRecipes = mutableStateOf<List<Recipe>>(listOf())
    val favoriteRecipes: State<List<Recipe>> = _favoriteRecipes

    //block to load favorites when viewmodel is created
    init {
        loadFavorites()
    }

    //load favorite recipes from shared preferences
    private fun loadFavorites() {
        val favoritesJson = sharedPreferences.getString("favorites", null)
        if (favoritesJson != null) {
            val type = object : TypeToken<List<Recipe>>() {}.type
            _favoriteRecipes.value = gson.fromJson(favoritesJson, type)
        }
    }
    // status of a recipe in favorite and update list in shared preferences
    fun toggleFavorite(recipe: Recipe) {
        val updatedRecipe = recipe.copy(isFavorited = !recipe.isFavorited)

        myRecipes.value = myRecipes.value?.map {
            if (it.id == recipe.id) updatedRecipe else it
        }

        if (updatedRecipe.isFavorited) {
            _favoriteRecipes.value = _favoriteRecipes.value + updatedRecipe
        } else {
            _favoriteRecipes.value = _favoriteRecipes.value.filter { it.id != recipe.id }
        }
        saveFavorites()
    }
    //save the current list of favorite recipes to shared preferences
    private fun saveFavorites() {
        val editor = sharedPreferences.edit()
        val favoritesJson = gson.toJson(_favoriteRecipes.value)
        editor.putString("favorites", favoritesJson)
        editor.apply()
    }
    //factory class to instantiate viewmodel with a context
    class RecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    //search for recipes based on ingredient and serving amount
    fun searchRecipesByIngredientAndServing(ingredient: String, servingAmount: String?) {
        viewModelScope.launch {
            val response = makeAPICall(ingredient)
            if (response.isSuccessful) {
                val data = response.body?.string()
                data?.let {
                    val type = object : TypeToken<List<Recipe>>() {}.type
                    var recipesList = Gson().fromJson<List<Recipe>>(it, type)
                    if (servingAmount != null) {
                        recipesList = recipesList.filter { recipe -> recipe.servings == servingAmount }
                    }
                    recipesList = recipesList.map { recipe ->
                        val uniqueID = UUID.randomUUID().toString()
                        val imageFallback = async(Dispatchers.IO) { fetchImageFromPexels(recipe.title) }
                        val imageUrl = imageFallback.await() ?: "default_image_url"
                        recipe.copy(id = uniqueID, imageUrl = imageUrl)
                    }
                    myRecipes.value = recipesList
                    _navigateToRecipes.value = ingredient
                }
            } else {
                myErrorMessages.value = "Failed to get data. Error code: ${response.code}"
            }
        }
    }
    //clear navigation trigger after navigating to a recipe
    fun onNavigatedToRecipes() {
        _navigateToRecipes.value = null
    }
    //search for recipes based on a search query
    fun searchRecipes(searchQuery: String) {
        viewModelScope.launch {
            try {
                val response = makeAPICall(searchQuery)
                val data = response.body?.string()

                if (data != null) {
                    val type = object : TypeToken<List<Recipe>>() {}.type
                    var recipesList = Gson().fromJson<List<Recipe>>(data, type)

                    recipesList = recipesList.map { recipe ->
                        val uniqueID = UUID.randomUUID().toString() // unique ID made random by ourself since api doesnt have
                        val imageFallback = async(Dispatchers.IO) { fetchImageFromPexels(recipe.title) }
                        val imageUrl = imageFallback.await() ?: "default_image_url"
                        recipe.copy(id = uniqueID, imageUrl = imageUrl)
                    }

                    myRecipes.value = recipesList
                } else {
                    myErrorMessages.value = "Failed to get data"
                }
            } catch (e: Exception) {
                myErrorMessages.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }
    // this function fetch generated images from Pexel API
    private suspend fun fetchImageFromPexels(query: String): String? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.pexels.com/v1/search?query=$query&per_page=1")
            .addHeader("Authorization", "gRiSNHpatgu0b7CMypuPFJaNQ9HDjT4lnaCfQyCHs2pelVZs26Hp4n0g")
            .build()

        try {
            SingletonClient.httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body?.string())
                    return@withContext jsonResponse.getJSONArray("photos")
                        .getJSONObject(0)
                        .getJSONObject("src")
                        .getString("medium")
                }
                return@withContext null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    //call api for recipe
    private suspend fun makeAPICall(query: String): Response = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://recipe-by-api-ninjas.p.rapidapi.com/v1/recipe?query=$query")
            .get()
            .addHeader("X-RapidAPI-Key", "4139f5fdd0msh348f64975982638p16bc88jsnd920aa37ec27")
            .addHeader("X-RapidAPI-Host", "recipe-by-api-ninjas.p.rapidapi.com")
            .build()

        return@withContext SingletonClient.httpClient.newCall(request).execute()
    }

    // find the recipe by its id
    fun findRecipeById(id: String?): Recipe? {
        return recipes.value?.find { it.id == id }
    }

}


