package com.example.recipes_test_app.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.recipes_test_app.data.db.RecipeEntity
import com.example.recipes_test_app.domain.usecases.GetCachedRecipesUseCase
import com.example.recipes_test_app.domain.usecases.GetRandomRecipesUseCase
import com.example.recipes_test_app.domain.usecases.InsertRecipesCache
import com.example.recipes_test_app.features.networkAvailabilityFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
) : ViewModel() {

    private val _recipesState = mutableStateOf<Resource<List<Recipe>>>(Resource.Loading())
    val recipesState: State<Resource<List<Recipe>>> get() = _recipesState

    private val currentRecipes = mutableListOf<Recipe>()

    // Теперь это State
    private val _isLoadingMore = mutableStateOf(false)
    val isLoadingMore: State<Boolean> get() = _isLoadingMore


    init {
        loadMoreRecipes()
    }

    fun loadMoreRecipes() {
        if (_isLoadingMore.value) return

        _isLoadingMore.value = true

        viewModelScope.launch {
            try {
                val newRecipes = getRandomRecipesUseCase()
                currentRecipes.addAll(newRecipes)
                _recipesState.value = Resource.Success(currentRecipes.toList())
            } catch (e: Exception) {
                _recipesState.value = Resource.Error(e.localizedMessage)
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

}
