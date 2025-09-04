package com.iti.graduation.recipeapp.data.remote

import com.iti.graduation.recipeapp.data.model.Categories
import com.iti.graduation.recipeapp.data.model.Countries
import com.iti.graduation.recipeapp.data.model.Meals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("lookup.php")
    suspend fun getMealByID(@Query("i") id: String): Response<Meals>

    @GET("search.php")
    suspend fun getMealByFirstLetter(@Query("f") letter: Char): Response<Meals>

    @GET("search.php")
    suspend fun getMealBySearch(@Query("s") keyword: String): Response<Meals>

    @GET("random.php")
    suspend fun getRandomMeal(): Response<Meals>

    @GET("categories.php")
    suspend fun getAllCategories(): Response<Categories>

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): Response<Meals>


    @GET("list.php")
    suspend fun getAllCountries(@Query("a") area: String = "list"): Response<Countries>

    @GET("filter.php")
    suspend fun getMealsByCountry(@Query("a") country: String): Response<Meals>



}