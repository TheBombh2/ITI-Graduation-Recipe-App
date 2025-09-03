package com.iti.graduation.recipeapp.ui.detail

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
class RecipeDetailViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _meal = MutableLiveData<Meal?>()
    val meal: LiveData<Meal?> get() = _meal

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getMealDetails(mealId: String) {
        viewModelScope.launch {
            val mealDetails = mealRepository.getMealByID(mealId)
            _meal.value = mealDetails

            // Check if meal is favorite
            mealDetails?.let {
                checkIfFavorite(it)
            }
        }
    }

    private fun checkIfFavorite(meal: Meal) {
        viewModelScope.launch {
            val favoriteMeals = mealRepository.getFavoriteMeals()
            _isFavorite.value = favoriteMeals.any { it.idMeal == meal.idMeal }
        }
    }

    fun toggleFavorite() {
        _meal.value?.let { meal ->
            viewModelScope.launch {
                if (_isFavorite.value == true) {
                    mealRepository.removeFavorite(meal)
                    _isFavorite.value = false
                } else {
                    mealRepository.addMealToFavorites(meal)
                    _isFavorite.value = true
                }
            }
        }
    }
}