package com.iti.graduation.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.iti.graduation.recipeapp.data.repository.AuthRepository
import com.iti.graduation.recipeapp.utility.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class MainSplashActivity : AppCompatActivity() {
    @Inject lateinit var sharedPrefManager: SharedPrefManager
    @Inject lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        lifecycleScope.launch {
            delay(1500)
            val isLoggedIn = sharedPrefManager.isLoggedIn()
            if(isLoggedIn){
                val intent = Intent(this@MainSplashActivity, RecipeActivity::class.java)
                val user =  authRepository.getUserById(sharedPrefManager.getLoggedInUserId()!!)
                intent.putExtra("user_id", user!!.id)
                intent.putExtra("user_name", user.name)
                startActivity(intent)
            }
            else{
                startActivity(Intent(this@MainSplashActivity, AuthActivity::class.java))
            }
            finish()
        }
    }
}