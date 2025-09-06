package com.iti.graduation.recipeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.graduation.recipeapp.data.model.Category
import com.iti.graduation.recipeapp.data.model.Country
import com.iti.graduation.recipeapp.databinding.ItemFilterOptionBinding
import com.iti.graduation.recipeapp.R

class FilterAdapter<T>(
    private var items: List<T>,
    private val onItemClick: (T) -> Unit
) : RecyclerView.Adapter<FilterAdapter<T>.FilterViewHolder>() {

    inner class FilterViewHolder(val binding: ItemFilterOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
            when (item) {
                is Category -> {
                    binding.spacer.visibility = View.VISIBLE
                    binding.tvFilterName.text = item.strCategory
                    Glide.with(binding.root)
                        .load(item.strCategoryThumb)
                        .placeholder(R.drawable.placeholder_image)
                        .into(binding.ivFilterImage)
                }
                is Country -> {
                    binding.tvFilterName.text = item.strArea
                    binding.ivFilterImage.visibility = View.GONE
                    binding.spacer.visibility = View.GONE
                }
            }

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


}
