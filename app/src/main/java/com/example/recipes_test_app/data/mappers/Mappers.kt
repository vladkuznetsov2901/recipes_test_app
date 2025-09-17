package com.example.recipes_test_app.data.mappers

import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.models.Recipe


// DTO → Domain
fun RecipeDTO.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = image,
        description = summary,
        instructions = instructions,
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}

fun RecipeDTO.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = image,
        description = summary,
        instructions = instructions,
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}

// Domain → Entity
fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        description = description,
        instructions = instructions,
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}

// Entity → Domain
fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        description = description,
        instructions = instructions,
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}
