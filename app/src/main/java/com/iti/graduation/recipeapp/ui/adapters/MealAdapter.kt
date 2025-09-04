package com.iti.graduation.recipeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.graduation.recipeapp.data.model.Meal
import com.iti.graduation.recipeapp.databinding.ItemMealBinding
import com.iti.graduation.recipeapp.R

class MealAdapter(
    private val onItemClick: (Meal) -> Unit,
    private val onRemoveClick: ((Meal) -> Unit)? = null, // nullable
    private val onRemoveWithConfirmation: ((Meal, () -> Unit) -> Unit)? = null // New parameter for confirmation
) : ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MealViewHolder(
            ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MealViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.apply {
                tvMealName.text = meal.strMeal

                Glide.with(itemView.context)
                    .load(meal.strMealThumb)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(ivMeal)

                // Navigate to detail
                root.setOnClickListener { onItemClick(meal) }

                // Show remove button only if onRemoveClick exists
                if (onRemoveClick != null || onRemoveWithConfirmation != null) {
                    ivFavorite.visibility = View.VISIBLE
                    ivFavorite.setOnClickListener {
                        if (onRemoveWithConfirmation != null) {
                            // Use confirmation dialog
                            onRemoveWithConfirmation.invoke(meal) {
                                // This lambda will be called after confirmation
                                onRemoveClick?.invoke(meal)
                            }
                        } else {
                            // Direct removal (fallback)
                            onRemoveClick?.invoke(meal)
                        }
                    }
                } else {
                        ivFavorite.visibility = View.GONE
                    }
                }
            }
        }
    }
    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) = oldItem.idMeal == newItem.idMeal
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) = oldItem == newItem
    }
