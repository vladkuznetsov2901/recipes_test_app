package com.example.recipes_test_app.domain.usecases

import com.example.recipes_test_app.data.mappers.toDomain
import com.example.recipes_test_app.data.mappers.toEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.NetworkChecker
import com.example.recipes_test_app.domain.repository.RecipesRepository
import javax.inject.Inject

class SearchRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipesRepository,
    private val networkChecker: NetworkChecker
) {
    suspend operator fun invoke(query: String): List<Recipe> {
        val localResults = recipeRepository.searchLocalRecipes(query)
        if (localResults.isNotEmpty()) {
            return localResults.map { it.toDomain() }
        }

        if (networkChecker.isNetworkAvailable()) {
            val remoteResults = recipeRepository.searchRemoteRecipes(query)
            val entities = remoteResults.map { it.toEntity() }
            recipeRepository.insertRecipesCache(entities)
            return entities.map { it.toDomain() }
        }

        return emptyList()
    }
}