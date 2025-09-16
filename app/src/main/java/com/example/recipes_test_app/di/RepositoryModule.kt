package com.example.recipes_test_app.di

import android.content.Context
import com.example.recipes_test_app.data.db.AppDatabase
import com.example.recipes_test_app.data.db.RecipeDao
import com.example.recipes_test_app.data.repository.RecipesRepositoryImpl
import com.example.recipes_test_app.domain.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun getRecipesRepository(appDatabase: AppDatabase, recipeDao: RecipeDao) : RecipesRepository {
        return RecipesRepositoryImpl(appDatabase, recipeDao)
    }

}