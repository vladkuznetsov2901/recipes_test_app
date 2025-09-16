package com.example.recipes_test_app.data.mappers

import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.models.Recipe


fun RecipeDTO.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = image,
        description = summary.replace(Regex("<.*?>"), "")
    )
}

// DTO -> Entity
fun RecipeDTO.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.image,
        description = this.summary.replace(Regex("<.*?>"), "")
    )
}

fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = this.id,
    title = this.title,
    imageUrl = this.imageUrl,
    description = this.description
)