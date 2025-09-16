package com.example.recipes_test_app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.recipes_test_app.data.api.Api
import com.example.recipes_test_app.data.db.AppDatabase
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.data.mappers.toEntity

@OptIn(ExperimentalPagingApi::class)
class RecipesRemoteMediator(
    private val api: Api,
    private val db: AppDatabase
) : RemoteMediator<Int, RecipeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) 1 else lastItem.id / state.config.pageSize + 1
                }
            }

            val response = api.getRandomRecipes(number = state.config.pageSize)
            val entities = response.recipes.map { it.toEntity() }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.recipeDao().clearAll()
                }
                db.recipeDao().insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}

