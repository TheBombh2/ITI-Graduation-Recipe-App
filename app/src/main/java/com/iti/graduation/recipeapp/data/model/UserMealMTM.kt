package com.iti.graduation.recipeapp.data.model

import androidx.room.Entity

@Entity(
    tableName = "user_meals",
    primaryKeys = ["userId","mealId"]
)
data class UserMealMTM(
    val userId: Int,
    val mealId: String
)