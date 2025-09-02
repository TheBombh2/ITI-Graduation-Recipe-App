package com.iti.graduation.recipeapp.data.remote

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

}