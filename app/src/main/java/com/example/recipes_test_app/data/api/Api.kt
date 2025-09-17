package com.example.recipes_test_app.data.api


import com.example.recipes_test_app.data.data.RandomRecipesResponse
import com.example.recipes_test_app.data.data.RecipeDTO
import com.example.recipes_test_app.domain.models.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("apiKey") apiKey: String = API_KEY3
    ): RandomRecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("apiKey") apiKey: String = API_KEY3
    ): Recipe

    companion object {
        private const val API_KEY = "5f721c9bf6c149ca8e83c6c3b784e382"
        private const val API_KEY2 = "692bde1bc1634c20839ad8d298377dcb"
        private const val API_KEY3 = "406eb0a5cecd4d6cada86cd2b2881ebc"


        val retrofit: Api by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}