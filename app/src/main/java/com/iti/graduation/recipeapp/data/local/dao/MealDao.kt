package com.iti.graduation.recipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.data.model.UserMealMTM

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals")
    suspend fun getAllMeals():List<Meal>


    @Query("DELETE FROM user_meals WHERE userId = :userId AND mealId = :mealId")
    suspend fun removeUserMeal(userId: Int, mealId: String)

    @Transaction
    @Query("""
        SELECT meals.* FROM meals
        INNER JOIN user_meals ON meals.idMeal = user_meals.mealId
        WHERE user_meals.userId = :userId
    """)
    suspend fun getMealsForUser(userId: Int): List<Meal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserMeal(userMeal: UserMealMTM)

    @Query("SELECT EXISTS(SELECT 1 FROM user_meals WHERE userId = :userId AND mealId = :mealId)")
    suspend fun isMealFavoriteForUser(userId: Int, mealId: String): Boolean

}