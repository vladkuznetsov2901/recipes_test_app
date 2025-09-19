package com.example.recipes_test_app.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.recipes_test_app.R
import com.example.recipes_test_app.domain.models.Recipe
import com.example.recipes_test_app.domain.models.Resource
import com.example.recipes_test_app.presentation.viewmodels.MainViewModel


@Composable
fun RecipesScreen(onRecipeClick: (Int) -> Unit, viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.recipesState.value
    val searchState = viewModel.searchState.value
    var searchQuery by remember { mutableStateOf("") }


    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotBlank()) {
                    viewModel.searchRecipes(it)
                }
            },
            label = { Text(stringResource(R.string.search_recipes_text)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        CategorySelector(viewModel)

        if (searchQuery.isNotBlank()) {
            when (searchState) {
                is Resource.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is Resource.Error -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    searchState.message?.let {
                        Text(
                            stringResource(R.string.error_text, it),
                            color = Color.Red
                        )
                    }
                }

                is Resource.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(searchState.data.size) { index ->
                            RecipeCard(searchState.data[index]) {
                                onRecipeClick(searchState.data[index].id)
                            }
                        }
                    }
                }
            }
        } else {
            when (state) {
                is Resource.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is Resource.Error -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    state.message?.let {
                        Text(
                            stringResource(R.string.error_text, it),
                            color = Color.Red
                        )
                    }
                }

                is Resource.Success -> {
                    val recipes = state.data
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(recipes.size) { index ->
                            RecipeCard(recipes[index]) {
                                onRecipeClick(recipes[index].id)
                            }

                            if (index == recipes.lastIndex && !viewModel.isLoadingMore.value) {
                                LaunchedEffect(Unit) {
                                    viewModel.loadMoreRecipes()
                                }
                            }
                        }

                        if (viewModel.isLoadingMore.value) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CategorySelector(
    viewModel: MainViewModel
) {
    val selectedCategory by viewModel.selectedCategory

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        viewModel.categories.forEachIndexed { index, category ->
            SegmentedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = viewModel.categories.size
                ),
                onClick = { viewModel.selectCategory(category) },
                selected = category == selectedCategory
            ) {
                Text(category)
            }
        }
    }
}
