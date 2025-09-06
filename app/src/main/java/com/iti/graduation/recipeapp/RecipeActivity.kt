package com.iti.graduation.recipeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.iti.graduation.recipeapp.databinding.ActivityRecipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController

    var userId:Int? = null
    var userName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("user_id",-1)
        userName = intent.getStringExtra("user_name")

        setupNavigation()
    }

    private fun setupNavigation() {
        // Find the NavHostFragment from the layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup bottom navigation with navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Listener to hide bottom nav on splash, show on other screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    //Navigate to RecipeDetailFragment with a mealId argument

    fun navigateToDetail(mealId: String) {
        val bundle = Bundle().apply {
            putString("mealId", mealId)
        }
        navController.navigate(R.id.action_global_recipeDetailFragment, bundle)
    }
    fun playVideo(url: String) {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse(url)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
