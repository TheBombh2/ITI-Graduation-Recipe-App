package com.iti.graduation.recipeapp.data.repository

import com.iti.graduation.recipeapp.data.local.dao.UserDao
import com.iti.graduation.recipeapp.data.model.User
import com.iti.graduation.recipeapp.utility.SharedPrefManager

class AuthRepository(val userDao: UserDao,val sharedPrefManager: SharedPrefManager) {


    //In your UI after you create User object with username, email and password
    //Pass it down to this function This inserts user to the database
    //Then to automatically sign them In use the loginUser function
    //You already have the user object so trigger this function with email and password
    // after registering
    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }


    //Takes email and password and returns found User object
    //If user exists it will save the user ID in sharedPreferences
    //Then If u want to login user automatically from splash Screen
    // Please look into utility/SharedPrefManager
    //If no user exists it returns null
    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.login(email, password)
        return if(user != null){
            saveLoginState(user.id)
            user
        }else{
            null
        }
    }


    //Log out
    fun logout() {
        sharedPrefManager.logout()
    }


    fun saveLoginState(userID: Int) {
        sharedPrefManager.saveLoginInformation(userID)
    }


}