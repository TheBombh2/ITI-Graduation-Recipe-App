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

    fun getMealDetails(userId:Int = -1,mealId: String) {
        viewModelScope.launch {
            val mealDetails = mealRepository.getMealByID(mealId)
            _meal.value = mealDetails

            // Check if meal is favorite
            mealDetails?.let {
                checkIfFavorite(userId,mealId)
            }
        }
    }

    private fun checkIfFavorite(userId:Int,mealId: String) {
        viewModelScope.launch {
            val favorite = mealRepository.isMealFavorite(userId,mealId)
            _isFavorite.value = favorite
        }
    }

    fun toggleFavorite(userId: Int) {
        _meal.value?.let { meal ->
            viewModelScope.launch {
                if (_isFavorite.value == true) {
                    mealRepository.removeFavorite(userId,meal)
                    mealRepository.removeMealFromFavorites(meal)
                    _isFavorite.value = false
                } else {
                    mealRepository.addMealToFavorites(userId,meal)
                    _isFavorite.value = true
                }
            }
        }
    }
}