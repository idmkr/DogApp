package com.example.dogapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogapp.data.model.Dog
import com.example.dogapp.databinding.ItemBreedBinding

class DogAdapter(
    private val onClick: (Dog) -> Unit,
    private val onFavorite: (Dog) -> Unit
) : RecyclerView.Adapter<DogAdapter.VH>() {

    companion object {
        private const val TAG = "DogAdapter"
    }

    private val items = mutableListOf<Dog>()

    fun submit(list: List<Dog>) {
        Log.d(TAG, "Submitting ${list.size} items")
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemBreedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: Dog) {
            Log.d(TAG, "Binding dog: ${dog.name}")

            binding.textBreedName.text = dog.name

            Glide.with(binding.imageBreed)
                .load(dog.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(binding.imageBreed)

            binding.btnFavorite.setImageResource(
                if (dog.isFavorite)
                    android.R.drawable.btn_star_big_on
                else
                    android.R.drawable.btn_star_big_off
            )

            binding.root.setOnClickListener {
                Log.d(TAG, "Item clicked: ${dog.name}")
                onClick(dog)
            }

            binding.btnFavorite.setOnClickListener {
                Log.d(TAG, "Favorite clicked for: ${dog.name}")
                onFavorite(dog)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBreedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}