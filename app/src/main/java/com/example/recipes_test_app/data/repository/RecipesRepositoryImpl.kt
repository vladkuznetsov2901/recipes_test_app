package com.example.recipes_test_app.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.recipes_test_app.data.api.Api.Companion.retrofit
import com.example.recipes_test_app.data.db.AppDatabase
import com.example.recipes_test_app.data.db.RecipeDao
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.data.mappers.toDomain
import com.example.recipes_test_app.data.mappers.toEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.RecipesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.recipes_test_app.features.networkAvailabilityFlow
import kotlinx.coroutines.flow.flatMapLatest

class RecipesRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val recipeDao: RecipeDao
) :
    RecipesRepository {
    override suspend fun getRandomRecipes(): List<Recipe> {
        val dto = retrofit.getRandomRecipes(number = 10)
        return dto.recipes.map { it.toDomain() }
    }

    override suspend fun insertRecipesCache(recipes: List<RecipeEntity>) {
        recipeDao.insertAll(recipes)
    }

    override suspend fun getRecipeById(id: Int): Recipe {
        val local = recipeDao.getRecipeById(id)
        if (local != null) {
            return local
        }

        val remote = retrofit.getRecipeById(id)
        recipeDao.insert(remote.toEntity())
        return remote
    }


    override suspend fun searchRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }
}