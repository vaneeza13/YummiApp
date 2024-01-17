package com.example.yummiapp.model

import java.util.UUID

//recipe data
data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val ingredients: String,
    val servings: String,
    val instructions: String,
    var imageUrl: String = "default_image_url",
    var isFavorited: Boolean = false

)
