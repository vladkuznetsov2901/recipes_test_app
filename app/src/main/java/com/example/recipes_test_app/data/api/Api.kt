package com.example.recipes_test_app.data.api


import com.example.recipes_test_app.domain.models.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("tags") tags: String? = null,
        @Query("exclude-tags") excludeTags: String? = null,
        @Query("apiKey") apiKey: String = API_KEY
    ): Recipe

    companion object {
        private const val API_KEY = "5f721c9bf6c149ca8e83c6c3b784e382"

        val retrofit: Api by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/random")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}