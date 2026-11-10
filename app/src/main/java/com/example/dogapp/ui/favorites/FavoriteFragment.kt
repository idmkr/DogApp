package com.example.dogapp.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.R
import com.example.dogapp.data.repository.DogRepository
import com.example.dogapp.databinding.FragmentFavoriteBinding
import com.example.dogapp.ui.adapter.DogAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    companion object {
        private const val TAG = "FavoriteFragment"
    }

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: DogAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FavoriteFragment onViewCreated")

        binding = FragmentFavoriteBinding.bind(view)

        adapter = DogAdapter(
            onClick = { dog ->
                Log.d(TAG, "Favorite dog clicked: ${dog.name}")
                // Навигация к деталям избранной собаки через NavController
                navigateToDetails(dog.id)
            },
            onFavorite = { dog ->
                Log.d(TAG, "Toggle favorite for: ${dog.name}")
                DogRepository.toggleFavorite(dog.id)
                adapter.submit(DogRepository.getFavorites())
            }
        )

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FavoriteFragment onResume")
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = DogRepository.getFavorites()
        Log.d(TAG, "Loaded ${favorites.size} favorites")
        adapter.submit(favorites)

        if (favorites.isEmpty()) {
            Log.d(TAG, "No favorites found")
        }
    }

    private fun navigateToDetails(dogId: String) {
        try {
            // Используем action для навигации из избранного
            val action = FavoriteFragmentDirections.actionFavoritesToDetails()
            val bundle = Bundle().apply {
                putString("dogId", dogId)
            }
            findNavController().navigate(R.id.action_favorites_to_details, bundle)
            Log.d(TAG, "Navigated to details from favorites")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation error from favorites: ${e.message}", e)
        }
    }
}