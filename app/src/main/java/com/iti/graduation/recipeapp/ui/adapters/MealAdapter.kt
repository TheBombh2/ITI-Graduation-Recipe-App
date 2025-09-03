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

class MealAdapter(
    private val onItemClick: (Meal) -> Unit,
    private val onRemoveClick: ((Meal) -> Unit)? = null // nullable
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
                    .into(ivMeal)

                // Navigate to detail
                root.setOnClickListener { onItemClick(meal) }

                // Show remove button only if onRemoveClick exists
                if (onRemoveClick != null) {
                    ivFavorite.visibility = View.VISIBLE
                    ivFavorite.setOnClickListener { onRemoveClick.invoke(meal) }
                } else {
                    ivFavorite.visibility = View.GONE
                }
            }
        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) = oldItem.idMeal == newItem.idMeal
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) = oldItem == newItem
    }
}
