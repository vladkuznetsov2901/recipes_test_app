package com.example.recipes_test_app.domain.usecases

import android.content.Context
import androidx.paging.PagingData
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.data.mappers.toDomain
import com.example.recipes_test_app.data.mappers.toEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.NetworkChecker
import com.example.recipes_test_app.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRandomRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val networkChecker: NetworkChecker
) {

    operator fun invoke(): Flow<List<Recipe>> = flow {
        val cached = recipesRepository.getCachedRecipes()
        if (cached.isNotEmpty()) emit(cached.map { it.toDomain() })

        try {
            if (networkChecker.isNetworkAvailable()) {
                val remote = recipesRepository.getRemoteRecipes()
                val entities = remote.map { it.toEntity() }
                recipesRepository.insertRecipesCache(entities)
                emit(entities.map { it.toDomain() })
            }
        } catch (_: Exception) {
        }
    }

}