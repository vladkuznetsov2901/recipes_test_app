package com.example.recipes_test_app.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.recipes_test_app.data.api.Api.Companion.retrofit
import com.example.recipes_test_app.data.data.RecipeDTO
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class RecipesRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) :
    RecipesRepository {

    override suspend fun getCachedRecipes(): List<RecipeEntity> {
        return recipeDao.getCachedRecipes()
    }

    override suspend fun getRemoteRecipes(): List<RecipeDTO> {
        return retrofit.getRandomRecipes(number = 10).recipes
    }

    override suspend fun insertRecipesCache(recipes: List<RecipeEntity>) {
        recipeDao.insertAll(recipes.map { it })
    }

    override suspend fun getRecipeById(id: Int): Recipe {
        val local = recipeDao.getRecipeById(id)
        if (local != null) {
            return local.toDomain()
        }

        val remote = retrofit.getRecipeById(id)
        Log.d("getRecipeById", "getRecipeById: $remote")
        recipeDao.insert(remote.toEntity())
        return remote.toDomain()
    }


    override suspend fun searchRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }
}