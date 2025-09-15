package com.example.recipes_test_app.data.mappers

import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.domain.models.Recipe


fun RecipeDTO.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = image,
        description = summary.replace(Regex("<.*?>"), "")
    )
}
