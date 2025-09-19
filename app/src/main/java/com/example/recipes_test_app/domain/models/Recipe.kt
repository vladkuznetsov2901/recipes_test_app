package com.example.recipes_test_app.domain.models

import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: Int,
    val title: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("summary") val description: String,
    val instructions: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val dishTypes: List<String>
)