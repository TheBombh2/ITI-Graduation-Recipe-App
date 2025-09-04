package com.iti.graduation.recipeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iti.graduation.recipeapp.databinding.FragmentHomeBinding
import com.iti.graduation.recipeapp.ui.adapters.MealAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var popularMealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRandomMealClick()
        observeData()
        viewModel.getRandomMeal()
        viewModel.getPopularMeals()
    }

    private fun setupRecyclerView() {
        // For popular meals (horizontal layout)
        popularMealAdapter = MealAdapter(
            onItemClick = { meal ->
                navigateToDetail(meal.idMeal)
            }
        )

        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        val actionId = com.iti.graduation.recipeapp.R.id.action_homeFragment_to_recipeDetailFragment
        val bundle = Bundle().apply { putString("mealId", mealId) }
        findNavController().navigate(actionId, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
