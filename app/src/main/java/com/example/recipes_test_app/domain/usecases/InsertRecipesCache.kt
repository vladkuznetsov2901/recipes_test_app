package com.example.recipes_test_app.domain.usecases

import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class InsertRecipesCache @Inject constructor(private val recipesRepository: RecipesRepository) {

    suspend operator fun invoke(recipes: List<Recipe>) {}

}