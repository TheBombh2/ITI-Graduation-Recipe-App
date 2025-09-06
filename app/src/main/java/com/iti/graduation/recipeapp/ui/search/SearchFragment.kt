package com.iti.graduation.recipeapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.graduation.recipeapp.RecipeActivity
import com.iti.graduation.recipeapp.databinding.FragmentSearchBinding
import com.iti.graduation.recipeapp.ui.adapters.MealAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeData()

        // Load ALL meals initially
        viewModel.loadAllMeals()
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(
            onItemClick = { meal ->
                (activity as? RecipeActivity)?.navigateToDetail(meal.idMeal)
            }
        )

        binding.rvSearchResults.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mealAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    when (binding.filterChipGroup.checkedChipId) {
                        com.iti.graduation.recipeapp.R.id.chipCategory -> viewModel.searchByCategory(it)
                        com.iti.graduation.recipeapp.R.id.chipIngredient -> viewModel.searchByIngredient(it)
                        com.iti.graduation.recipeapp.R.id.chipCountry -> viewModel.searchByCountry(it)
                        else -> viewModel.searchMeals(it) // default search
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // ✅ Reset to ALL meals when search is cleared
                    viewModel.loadAllMeals()
                }
                return true
            }
        })
    }

    private fun observeData() {
        viewModel.searchResults.observe(viewLifecycleOwner) { meals ->
            mealAdapter.submitList(meals)
            binding.tvEmpty.visibility = if (meals.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
