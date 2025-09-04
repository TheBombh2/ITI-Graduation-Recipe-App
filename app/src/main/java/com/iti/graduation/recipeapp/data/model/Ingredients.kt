package com.iti.graduation.recipeapp.data.model

import com.google.gson.annotations.SerializedName

data class Ingredients(
    @SerializedName("meals") val ingredients: List<Ingredient>
)