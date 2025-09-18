package com.example.recipes_test_app.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.data.db.RecipeEntity

import com.example.recipes_test_app.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {

    suspend fun getCachedRecipes(): List<RecipeEntity>
    suspend fun getRemoteRecipes(): List<RecipeDTO>
    suspend fun searchRecipes() : List<Recipe>

    suspend fun insertRecipesCache(recipes: List<RecipeEntity>)

    suspend fun getRecipeById(id: Int): Recipe

}