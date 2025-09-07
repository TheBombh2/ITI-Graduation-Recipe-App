package com.iti.graduation.recipeapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.graduation.recipeapp.data.model.Category
import com.iti.graduation.recipeapp.data.model.Country
import com.iti.graduation.recipeapp.data.model.Ingredient
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

   lateinit var  allMealsFirstLoad:List<Meal>

    private val _searchResults = MutableLiveData<List<Meal>>()
    val searchResults: LiveData<List<Meal>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Optional filters data (if you want to populate dropdowns/spinners later)
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    //private val _ingredients = MutableLiveData<List<Ingredient>>()
    //val ingredients: LiveData<List<Ingredient>> get() = _ingredients

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    init {
        loadAllMeals()
        loadFilterOptions()
    }

    /** 🔍 Search by free query (default search) */
    fun searchMeals(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = mealRepository.searchForMeals(query)?.meals ?: emptyList()
                _searchResults.value = results
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 🔍 Search by Category */
    fun searchByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = mealRepository.getMealByCategory(category)?.meals ?: emptyList()
                _searchResults.value = results
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 🔍 Search by Ingredient */
    fun searchByIngredient(ingredient: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = mealRepository.getMealsByIngredient(ingredient)?.meals ?: emptyList()
                _searchResults.value = results
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 🔍 Search by Country */
    fun searchByCountry(country: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = mealRepository.getMealByCountry(country)?.meals ?: emptyList()
                _searchResults.value = results
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 🎲 Load 10 random meals on start */
    fun loadAllMeals() {
        if(!_searchResults.value.isNullOrEmpty()){
            _searchResults.value = allMealsFirstLoad
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allMeals = mealRepository.getAllMeals()?.meals ?: emptyList()
                allMealsFirstLoad = allMeals
                _searchResults.value = allMeals
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 📥 Load available filter options (categories, ingredients, countries) */
    private fun loadFilterOptions() {
        viewModelScope.launch {
            try {
                val categoriesList = mealRepository.getAllCategoriesTypes()
                _categories.value = categoriesList?.categories

                //val ingredientsList = mealRepository.getAllIngredients()
                //_ingredients.value = ingredientsList?.ingredients

                val countriesList = mealRepository.getAllCountries()
                _countries.value = countriesList?.countries

            } catch (_: Exception) {
                _categories.value = emptyList()
                _countries.value = emptyList()
            }
        }
    }
}
