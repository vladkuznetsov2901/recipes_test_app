package com.example.recipes_test_app.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.recipes_test_app.data.api.Api
import com.example.recipes_test_app.data.api.Api.Companion.retrofit
import com.example.recipes_test_app.data.mappers.toDomain
import com.example.recipes_test_app.domain.models.Recipe

class RecipesPagingSource(
    private val api: Api
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        return try {
            val response = api.getRandomRecipes(number = 10)
            LoadResult.Page(
                data = response.recipes.map { it.toDomain() },
                prevKey = null,
                nextKey = (params.key ?: 1) + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? = null
}


