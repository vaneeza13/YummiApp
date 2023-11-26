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
import java.util.UUID

class RecipeViewModel : ViewModel() {
    private val _recipes = mutableStateOf<List<Recipe>?>(null)
    val recipes: State<List<Recipe>?> = _recipes
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                val response = makeAPICall(query)
                val data = response.body?.string()


                if (data != null) {
                    val type = object : TypeToken<List<Recipe>>() {}.type
                    var recipesList = Gson().fromJson<List<Recipe>>(data, type)

                    recipesList = recipesList.map { recipe ->
                        val uniqueID = UUID.randomUUID().toString() // unique ID made random by ourself since api doesnt have
                        val imageDeferred = async(Dispatchers.IO) { fetchImageFromPexels(recipe.title) }
                        val imageUrl = imageDeferred.await() ?: "default_image_url"
                        recipe.copy(id = uniqueID, imageUrl = imageUrl) // Assign ID and image URL
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

    private suspend fun fetchImageFromPexels(query: String): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.pexels.com/v1/search?query=$query&per_page=1")
            .addHeader("Authorization", "gRiSNHpatgu0b7CMypuPFJaNQ9HDjT4lnaCfQyCHs2pelVZs26Hp4n0g")
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

    private suspend fun makeAPICall(query: String): Response = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://recipe-by-api-ninjas.p.rapidapi.com/v1/recipe?query=$query")
            .get()
            .addHeader("X-RapidAPI-Key", "4139f5fdd0msh348f64975982638p16bc88jsnd920aa37ec27")
            .addHeader("X-RapidAPI-Host", "recipe-by-api-ninjas.p.rapidapi.com")
            .build()

        return@withContext client.newCall(request).execute()
    }

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
    var imageUrl: String = "default_image_url"
)
