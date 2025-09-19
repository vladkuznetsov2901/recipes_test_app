package com.example.recipes_test_app.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.models.Resource
import com.example.recipes_test_app.domain.usecases.GetRandomRecipesUseCase
import com.example.recipes_test_app.domain.usecases.GetRecipeByIdUseCase
import com.example.recipes_test_app.domain.usecases.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _recipesState = mutableStateOf<Resource<List<Recipe>>>(Resource.Loading())
    val recipesState: State<Resource<List<Recipe>>> get() = _recipesState

    private val currentRecipes = mutableListOf<Recipe>()

    private val _isLoadingMore = mutableStateOf(false)
    val isLoadingMore: State<Boolean> get() = _isLoadingMore

    private val _uiState = MutableStateFlow<Resource<Recipe>>(Resource.Loading())
    val uiState: StateFlow<Resource<Recipe>> = _uiState

    private val _searchState = mutableStateOf<Resource<List<Recipe>>>(Resource.Loading())
    val searchState: State<Resource<List<Recipe>>> get() = _searchState

    val categories = listOf("lunch", "main course", "main dish", "dinner")
    private val _selectedCategory = mutableStateOf<String?>(null)
    val selectedCategory: State<String?> get() = _selectedCategory

    init {
        loadMoreRecipes()
    }



    fun loadMoreRecipes() {
        if (_isLoadingMore.value) return

        _isLoadingMore.value = true

        viewModelScope.launch {
            getRandomRecipesUseCase().collect { newRecipes ->
                try {
                    val uniqueNew = newRecipes.filter { new ->
                        currentRecipes.none { it.id == new.id }
                    }
                    currentRecipes.addAll(uniqueNew)
                    _recipesState.value = Resource.Success(currentRecipes.toList())
                } catch (e: Exception) {
                    _recipesState.value = Resource.Error(e.localizedMessage)
                } finally {
                    _isLoadingMore.value = false
                }
            }
        }
    }



    fun loadRecipeById(id: Int) {
        viewModelScope.launch {
            try {
                val recipe = getRecipeByIdUseCase(id)
                _uiState.value = Resource.Success(recipe)
            } catch (e: Exception) {
                _uiState.value = Resource.Error("Нет соединения и данных нет в базе")
                Log.d("loadRecipeById", "loadRecipeById: ${e.message}")
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _searchState.value = Resource.Loading()
            try {
                val results = searchRecipesUseCase(query)
                _searchState.value = Resource.Success(results)
            } catch (e: Exception) {
                _searchState.value = Resource.Error(e.localizedMessage)
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        val filtered = if (category == null) {
            currentRecipes
        } else {
            currentRecipes.filter { it.dishTypes.contains(category) }
        }
        _recipesState.value = Resource.Success(filtered)
    }

}
