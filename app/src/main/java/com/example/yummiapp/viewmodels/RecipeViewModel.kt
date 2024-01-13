package com.example.yummiapp.viewmodels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class RecipeViewModel(private val context: Context) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("YummiAppPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val myRecipes = mutableStateOf<List<Recipe>?>(null)
    val recipes: State<List<Recipe>?> = myRecipes

    private val myErrorMessages = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = myErrorMessages

    private val _navigateToRecipes = mutableStateOf<String?>(null)
    val navigateToRecipes: State<String?> = _navigateToRecipes

    private val _favoriteRecipes = mutableStateOf<List<Recipe>>(listOf())
    val favoriteRecipes: State<List<Recipe>> = _favoriteRecipes

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val favoritesJson = sharedPreferences.getString("favorites", null)
        if (favoritesJson != null) {
            val type = object : TypeToken<List<Recipe>>() {}.type
            _favoriteRecipes.value = gson.fromJson(favoritesJson, type)
        }
    }

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

    private fun saveFavorites() {
        val editor = sharedPreferences.edit()
        val favoritesJson = gson.toJson(_favoriteRecipes.value)
        editor.putString("favorites", favoritesJson)
        editor.apply()
    }

    class RecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
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
    fun onNavigatedToRecipes() {
        _navigateToRecipes.value = null
    }

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
    // this function fetch generated images from Pexel API, uses singleton OkHttpClient
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

    //uses singleton OkHttpClient to call api for recipe
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


// Recipe data class
data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val ingredients: String,
    val servings: String,
    val instructions: String,
    var imageUrl: String = "default_image_url",
    var isFavorited: Boolean = false

)
