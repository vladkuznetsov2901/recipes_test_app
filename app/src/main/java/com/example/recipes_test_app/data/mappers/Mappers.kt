package com.example.recipes_test_app.data.mappers

import android.os.Build
import android.text.Html
import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.models.Recipe


// DTO → Domain
fun RecipeDTO.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = image ?: "",
        description = summary.stripHtml(),
        instructions = instructions.stripHtml(),
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}

fun String.stripHtml(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
}

fun RecipeDTO.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    title = title ?: "Без названия",
    imageUrl = image ?: "",
    description = summary.stripHtml() ?: "",
    instructions = instructions.stripHtml() ?: "",
    readyInMinutes = readyInMinutes ?: 0,
    servings = servings ?: 0
)


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
        imageUrl = imageUrl  ?: "",
        description = description,
        instructions = instructions?.stripHtml(),
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}
