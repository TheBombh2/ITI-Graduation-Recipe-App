package com.iti.graduation.recipeapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Meal>>()
    val searchResults: LiveData<List<Meal>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchMeals(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = mealRepository.searchForMeals(query)?.meals ?: emptyList()
                _searchResults.value = results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}