package com.example.recipes_test_app.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.recipes_test_app.data.db.RecipeEntity

import com.example.recipes_test_app.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {

    suspend fun getCachedRecipes(): List<RecipeEntity>
    suspend fun getRemoteRecipes(): List<Recipe>
    suspend fun insertRecipesCache(recipes: List<RecipeEntity>)
    suspend fun getRecipeById(id: Int): Recipe
    suspend fun searchLocalRecipes(query: String): List<RecipeEntity>
    suspend fun searchRemoteRecipes(query: String): List<Recipe>
}