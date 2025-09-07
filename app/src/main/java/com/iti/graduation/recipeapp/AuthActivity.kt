package com.iti.graduation.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.iti.graduation.recipeapp.data.model.User
import com.iti.graduation.recipeapp.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
       setupNavigation()
    }

    private fun setupNavigation() {
        // Find the NavHostFragment from the layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController


    }


    fun navigateToRegister() {

        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun navigateToRecipeActivity(user: User) {

        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("user_id", user.id)
        intent.putExtra("user_name", user.name)
        startActivity(intent)
        finish()
    }


}