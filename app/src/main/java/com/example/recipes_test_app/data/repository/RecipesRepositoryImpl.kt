package com.example.recipes_test_app.data.repository

import android.util.Log
import com.example.recipes_test_app.data.api.Api.Companion.retrofit
import com.example.recipes_test_app.data.db.RecipeDao
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.data.mappers.toDomain
import com.example.recipes_test_app.data.mappers.toEntity
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.repository.RecipesRepository
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) :
    RecipesRepository {

    override suspend fun getCachedRecipes(): List<RecipeEntity> {
        return recipeDao.getCachedRecipes()
    }

    override suspend fun getRemoteRecipes(): List<Recipe> {
        return retrofit.getRandomRecipes(number = 10).recipes.map { it.toDomain() }
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

    override suspend fun searchLocalRecipes(query: String): List<RecipeEntity> {
        return recipeDao.searchRecipes(query)
    }

    override suspend fun searchRemoteRecipes(query: String): List<Recipe> {
        return retrofit.searchRecipes(query).results.map { it.toDomain() }
    }

}