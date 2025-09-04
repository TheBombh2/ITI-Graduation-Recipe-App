package com.iti.graduation.recipeapp.ui.home

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
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _randomMeal = MutableLiveData<Meal?>()
    val randomMeal: LiveData<Meal?> get() = _randomMeal

    private val _popularMeals = MutableLiveData<List<Meal>>()
    val popularMeals: LiveData<List<Meal>> get() = _popularMeals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    // Store the random meal ID to prevent regeneration
    private var currentRandomMealId: String? = null

    init {
        // Load random meal only if not already loaded
        if (_randomMeal.value == null) {
            getRandomMeal()
        }
        getPopularMeals()
    }

    fun getRandomMeal() {
        // Don't reload if we already have a random meal
        if (_randomMeal.value != null) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val randomMeal = mealRepository.getRandomMeal()
                randomMeal?.let {
                    currentRandomMealId = it.idMeal
                    _randomMeal.value = it
                }
            } catch (e: Exception) {
                _randomMeal.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getPopularMeals() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allMeals = mealRepository.getAllMeals()?.meals ?: emptyList()

                // Shuffle list to get random items, then take 10
                val filteredMeals = allMeals
                    .filter { it.idMeal != currentRandomMealId }
                    .shuffled()
                    .take(10)
                _popularMeals.value = filteredMeals
            } catch (e: Exception) {
                _popularMeals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}