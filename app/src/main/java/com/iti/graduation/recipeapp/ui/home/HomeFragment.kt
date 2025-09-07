package com.iti.graduation.recipeapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.graduation.recipeapp.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iti.graduation.recipeapp.AuthActivity
import com.iti.graduation.recipeapp.RecipeActivity
import com.iti.graduation.recipeapp.databinding.FragmentHomeBinding
import com.iti.graduation.recipeapp.ui.adapters.MealAdapter
import com.iti.graduation.recipeapp.utility.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var popularMealAdapter: MealAdapter

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserName = (activity as RecipeActivity).userName
        currentUserName?.let {
            binding.toolbar.title = "Hi! ${it}"
        }

        setupRecyclerView()
        setupRandomMealClick()
        observeData()
        viewModel.getRandomMeal()
        viewModel.getPopularMeals()
        setupMenuOptions()
    }


    private fun setupMenuOptions(){
       binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener {
            item ->
            when (item.itemId) {
                R.id.action_sign_out -> {
                    sharedPrefManager.logout()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }

                R.id.action_about -> {
                    // ✅ Navigate to AboutFragment
                    navigateToAbout()
                    true
                }

                else -> false

            }
        }
    }



    private fun setupRecyclerView() {
        // For popular meals (horizontal layout)
        popularMealAdapter = MealAdapter(
            onItemClick = { meal ->
                navigateToDetail(meal.idMeal)
            }
        )

        binding.rvPopularMeals.apply {
            layoutManager = GridLayoutManager(requireContext(),2, LinearLayoutManager.VERTICAL, false)
            adapter = popularMealAdapter
        }
    }
    private fun setupRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            viewModel.randomMeal.value?.let { meal ->
                navigateToDetail(meal.idMeal)
            }
        }
    }
    private fun observeData() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
            meal?.let {
                binding.tvRandomMealName.text = it.strMeal
                Glide.with(requireContext())
                    .load(it.strMealThumb)
                    .centerCrop()
                    .into(binding.ivRandomMeal)
            }
        }

        viewModel.popularMeals.observe(viewLifecycleOwner) { meals ->
            popularMealAdapter.submitList(meals)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    private fun navigateToDetail(mealId: String) {
        val actionId = R.id.action_homeFragment_to_recipeDetailFragment
        val bundle = Bundle().apply { putString("mealId", mealId) }
        findNavController().navigate(actionId, bundle)
    }

    private fun navigateToAbout() {
        val actionId = R.id.action_homeFragment_to_aboutFragment
        findNavController().navigate(actionId)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
