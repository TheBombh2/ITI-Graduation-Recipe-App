package com.iti.graduation.recipeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.graduation.recipeapp.RecipeActivity
import com.iti.graduation.recipeapp.databinding.FragmentHomeBinding
import com.iti.graduation.recipeapp.ui.adapters.MealAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mealAdapter: MealAdapter

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
        observeMeals()
        viewModel.getAllMeals()
    }

    private fun setupRecyclerView() {
        // Pass only onItemClick; onRemoveClick is omitted → favorite icon hidden
        mealAdapter = MealAdapter(onItemClick = { meal ->
            (activity as? RecipeActivity)?.navigateToDetail(meal.idMeal)
        })

        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mealAdapter
        }
    }

    private fun navigateToDetail(mealId: String) {
        val actionId = com.iti.graduation.recipeapp.R.id.action_homeFragment_to_recipeDetailFragment
        val bundle = Bundle().apply { putString("mealId", mealId) }
        findNavController().navigate(actionId, bundle)
    }

    private fun observeMeals() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            mealAdapter.submitList(meals)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
