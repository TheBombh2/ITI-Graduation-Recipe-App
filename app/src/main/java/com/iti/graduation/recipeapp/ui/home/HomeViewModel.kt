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

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllMeals() {
        if(!_meals.value.isNullOrEmpty())
            return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allMeals = mealRepository.getAllMeals()?.meals ?: emptyList()
                _meals.value = allMeals
            } catch (e: Exception) {
                _meals.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}