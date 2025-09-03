package com.iti.graduation.recipeapp.ui.favorite
import com.iti.graduation.recipeapp.RecipeActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.graduation.recipeapp.databinding.FragmentFavoriteBinding
import com.iti.graduation.recipeapp.ui.adapters.MealAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()
        viewModel.getFavoriteMeals()
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(
            onItemClick = { meal ->
                (activity as? RecipeActivity)?.navigateToDetail(meal.idMeal)
            },
            onRemoveClick = { meal ->
                viewModel.removeFavorite(meal)
            }
        )

        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mealAdapter
        }
    }


    private fun observeFavorites() {
        viewModel.favoriteMeals.observe(viewLifecycleOwner) { meals ->
            mealAdapter.submitList(meals)
            binding.tvEmpty.visibility = if (meals.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}