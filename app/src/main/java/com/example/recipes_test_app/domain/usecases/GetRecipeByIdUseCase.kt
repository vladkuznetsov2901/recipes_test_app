package com.example.recipes_test_app.domain.usecases

import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.RecipesRepository
import javax.inject.Inject

class GetRecipeByIdUseCase  @Inject constructor(private val recipesRepository: RecipesRepository) {

    suspend operator fun invoke(id: Int): Recipe {
        return recipesRepository.getRecipeById(id)
    }

}