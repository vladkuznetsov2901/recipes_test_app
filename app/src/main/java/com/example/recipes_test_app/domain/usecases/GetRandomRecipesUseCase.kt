package com.example.recipes_test_app.domain.usecases

import androidx.paging.PagingData
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class GetRandomRecipesUseCase  @Inject constructor(private val recipesRepository: RecipesRepository) {

    suspend operator fun invoke(): List<Recipe> {
        return recipesRepository.getRandomRecipes()
    }

}