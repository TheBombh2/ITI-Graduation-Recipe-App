package com.iti.graduation.recipeapp.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("recipe_app_prefs", Context.MODE_PRIVATE)

    companion object{
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val USER_ID = "user_id"
    }

    fun saveLoginInformation(userID:Int){
        prefs.edit {
            putBoolean(IS_LOGGED_IN, true)
                .putInt(USER_ID, userID)
        }
    }


    //In Splash screen check if there is already a loggedIn user
    //If it's true get the user from the next function and continue to the home activity
    //if it's false continue to the login fragment
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    //Get LoggedIn user from sharedPref
    fun getLoggedInUserId(): Int? {
        return if (isLoggedIn()) prefs.getInt(USER_ID, -1).takeIf { it != -1 }
        else null
    }

    fun logout(){
        prefs.edit{
            clear().apply()
        }
    }
}