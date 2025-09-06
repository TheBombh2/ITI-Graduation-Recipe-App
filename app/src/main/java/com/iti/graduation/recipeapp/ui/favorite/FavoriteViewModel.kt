package com.iti.graduation.recipeapp.ui.favorite

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
class FavoriteViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _favoriteMeals = MutableLiveData<List<Meal>>()
    val favoriteMeals: LiveData<List<Meal>> get() = _favoriteMeals

    fun getFavoriteMeals(userId:Int) {
        viewModelScope.launch {
            val favorites = mealRepository.getFavoriteMeals(userId)
            _favoriteMeals.value = favorites
        }
    }

    fun removeFavorite(userId: Int,meal: Meal) {
        viewModelScope.launch {
            mealRepository.removeFavorite(userId,meal)
            mealRepository.removeMealFromFavorites(meal)
            getFavoriteMeals(userId)
        }
    }
}