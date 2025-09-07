package com.iti.graduation.recipeapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.iti.graduation.recipeapp.databinding.FragmentRecipeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.R
import com.iti.graduation.recipeapp.RecipeActivity

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeDetailViewModel by viewModels()

    private var mealId: String? = null

    var currentUserId:Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUserId = (activity as? RecipeActivity)?.userId ?: -1
        mealId = arguments?.getString("mealId")
        mealId?.let { viewModel.getMealDetails(userId =  currentUserId, mealId = it) }



        observeMealDetails()
        setupClickListeners()
    }

    private fun observeMealDetails() {
        viewModel.meal.observe(viewLifecycleOwner) { meal ->
            meal?.let { bindMealData(it) }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if(isFavorite){
                binding.btnFavorite.setImageResource(R.drawable.heart_filled)
            }
            else{
                binding.btnFavorite.setImageResource(R.drawable.heart_outlined)
            }
        }
    }

    private fun bindMealData(meal: Meal) {
        Glide.with(requireContext())
            .load(meal.strMealThumb)
            .into(binding.ivMeal)

        binding.tvMealName.text = meal.strMeal
        binding.tvCategory.text = meal.strCategory
        binding.tvArea.text = meal.strArea
        binding.tvInstructions.text = meal.strInstructions

        val ingredients = meal.getIngredientsWithMeasures()
        binding.tvIngredients.text = ingredients.joinToString("\n")
    }

    private fun setupClickListeners() {
        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite(userId =currentUserId )
        }

        // Play video using WebView overlay
        binding.btnPlayVideo.setOnClickListener {
            viewModel.meal.value?.strYoutube?.let { url ->
                binding.videoContainer.visibility = View.VISIBLE
                binding.webViewVideo.webViewClient = WebViewClient()
                binding.webViewVideo.settings.javaScriptEnabled = true
                binding.webViewVideo.settings.loadWithOverviewMode = true
                binding.webViewVideo.settings.useWideViewPort = true

                val embedUrl = url.replace("watch?v=", "embed/") + "?autoplay=1"
                binding.webViewVideo.loadUrl(embedUrl)
            }
        }

        binding.btnCloseVideo.setOnClickListener {
            binding.videoContainer.visibility = View.GONE
            binding.webViewVideo.loadUrl("about:blank")
        }

        binding.btnExpand.setOnClickListener {
            val isExpanded = binding.tvInstructions.maxLines != Int.MAX_VALUE
            binding.tvInstructions.maxLines = if (isExpanded) Int.MAX_VALUE else 5
            binding.btnExpand.text = if (isExpanded) "Show Less" else "Show More"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webViewVideo.loadUrl("about:blank") // stop video
        _binding = null
    }
}
