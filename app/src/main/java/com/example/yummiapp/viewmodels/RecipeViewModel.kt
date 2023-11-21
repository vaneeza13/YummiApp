package com.example.yummiapp.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class RecipeViewModel : ViewModel() {
    private val _recipes = mutableStateOf<List<Recipe>?>(null)
    private val _errorMessage = mutableStateOf<String?>(null)

    val recipes: State<List<Recipe>?> = _recipes
    val errorMessage: State<String?> = _errorMessage

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                val response = makeAPICall(query)
                val data = response.body?.string()

                if (data != null) {
                    val type = object : TypeToken<List<Recipe>>() {}.type
                    var recipesList = Gson().fromJson<List<Recipe>>(data, type)

                    // Fetch images for each recipe
                    recipesList = recipesList.map { recipe ->
                        // Launch a coroutine for each image fetch
                        val imageDeferred = async(Dispatchers.IO) { fetchImageFromPexels(recipe.title) }
                        val imageUrl = imageDeferred.await() ?: "default_image_url" // Use default URL if null
                        recipe.copy(imageUrl = imageUrl)
                    }

                    _recipes.value = recipesList
                } else {
                    _errorMessage.value = "Failed to fetch data"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    suspend fun fetchImageFromPexels(query: String): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.pexels.com/v1/search?query=$query&per_page=1")
            .addHeader("Authorization", "gRiSNHpatgu0b7CMypuPFJaNQ9HDjT4lnaCfQyCHs2pelVZs26Hp4n0g") // Your Pexels API Key
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null

                val jsonResponse = JSONObject(response.body?.string())
                val firstImageUrl = jsonResponse.getJSONArray("photos").getJSONObject(0).getJSONObject("src").getString("medium")

                return@withContext firstImageUrl
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}
private suspend fun makeAPICall(query: String): Response {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://recipe-by-api-ninjas.p.rapidapi.com/v1/recipe?query=$query")
        .get()
        .addHeader("X-RapidAPI-Key", "dea0d1e227mshdef66c06d9d9811p19a79ajsnd0169e064d08")
        .addHeader("X-RapidAPI-Host", "recipe-by-api-ninjas.p.rapidapi.com")
        .build()

    return withContext(Dispatchers.IO) {
        client.newCall(request).execute()
    }
}


// Recipe Model
data class Recipe(val title: String, val ingredients: String, val servings: String, val instructions: String,  val imageUrl: String = "default_image_url"
)