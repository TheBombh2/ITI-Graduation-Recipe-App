package com.iti.graduation.recipeapp.data.model

import com.google.gson.annotations.SerializedName

data class Countries(
   @SerializedName("meals") val countries: List<Country>
)