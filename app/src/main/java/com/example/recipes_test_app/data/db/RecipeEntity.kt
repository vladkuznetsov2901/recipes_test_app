package com.example.recipes_test_app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String?,
    val description: String,
    val instructions: String?,
    val readyInMinutes: Int?,
    val servings: Int?
)