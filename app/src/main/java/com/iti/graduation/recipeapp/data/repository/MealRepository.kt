package com.iti.graduation.recipeapp.data.repository

import com.iti.graduation.recipeapp.data.local.dao.MealDao
import com.iti.graduation.recipeapp.data.model.Categories
import com.iti.graduation.recipeapp.data.model.Category
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.data.model.Meals
import com.iti.graduation.recipeapp.data.remote.RetrofitInstance

class MealRepository(val mealDao: MealDao) {

    //All these are remote calls to the API

    //Provide ID to get a Meal Object
    suspend fun getMealByID(id: String) : Meal?{
        val response = RetrofitInstance.api.getMealByID(id)
        if(response.isSuccessful){
            return response.body()?.meals?.firstOrNull()
        }else{
            return null
        }
    }


    //This Returns a "Meals" object that has "meals" Value
    //This "meals" value is a list<Meal>
    //It contains all meals in the API
    suspend fun getAllMeals() : Meals?{
        val allMeals = mutableListOf<Meal>()
        for(letter in 'a'..'z'){
            try {
                val response = RetrofitInstance.api.getMealByFirstLetter(letter)
                if(response.isSuccessful){
                    val meals = response.body()?.meals
                    if(!meals.isNullOrEmpty()){
                        allMeals.addAll(meals)
                    }
                }
            }
            catch (e: Exception){

            }
        }
        return Meals(allMeals)
    }


    //Provide any String like "S" or "Shaw"
    //Any number of letters will work
    //Same as before it's a "Meals" object
    suspend fun searchForMeals(keyword: String) : Meals?{
        val response = RetrofitInstance.api.getMealBySearch(keyword)
        if(response.isSuccessful){
            return response.body()
        }else{
            return null
        }
    }


    //Returns a random "Meal" object
    suspend fun getRandomMeal() : Meal?{
        val response = RetrofitInstance.api.getRandomMeal()
        if(response.isSuccessful){
            return response.body()?.meals?.firstOrNull()
        }else{
            return null
        }
    }


    //This returns "Categories" Object that has "categories" value that is List<Category>
    //That value includes all categories
    suspend fun getAllCategoriesTypes() : Categories?{
        val response = RetrofitInstance.api.getAllCategories()
        if(response.isSuccessful){
            return response.body()
        }else{
            return null
        }
    }

    //Provide "Category" object and receive "Meals" object that has all meals
    // under provided category
    suspend fun getMealByCategory(category: Category) : Meals?{
        val response = RetrofitInstance.api.getMealsByCategory(category.strCategory)
        if(response.isSuccessful){
            return response.body()
        }else{
            return null
        }
    }

    //Local Database Functions


    //You provide Meal object and it gets saved
    suspend fun addMealToFavorites(meal: Meal) {
        mealDao.insertMeal(meal)
    }
    //You provide Meal object and it gets deleted
    suspend fun removeMealFromFavorites(meal: Meal) {
        mealDao.deleteMeal(meal)
    }
    suspend fun removeFavorite(meal: Meal) {
        mealDao.deleteMeal(meal)
    }
    //Gets all meals in database
    suspend fun getFavoriteMeals() = mealDao.getAllMeals()
}