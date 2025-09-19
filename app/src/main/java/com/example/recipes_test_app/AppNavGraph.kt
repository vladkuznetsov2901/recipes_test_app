package com.example.recipes_test_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipes_test_app.presentation.screens.RecipeDetailsScreen
import com.example.recipes_test_app.presentation.screens.RecipesScreen

@Composable
fun AppNavGraph(startDestination: String = "recipes") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("recipes") {
            RecipesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate("details/$recipeId")
                }
            )
        }

        // экран деталей
        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            RecipeDetailsScreen(
                id = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
