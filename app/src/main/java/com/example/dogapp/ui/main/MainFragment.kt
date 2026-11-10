package com.example.dogapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.R
import com.example.dogapp.data.repository.DogRepository
import com.example.dogapp.databinding.FragmentMainBinding
import com.example.dogapp.ui.adapter.DogAdapter
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private const val TAG = "MainFragment"
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: DogAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "MainFragment onViewCreated")

        binding = FragmentMainBinding.bind(view)

        adapter = DogAdapter(
            onClick = { dog ->
                Log.d(TAG, "Dog clicked: ${dog.name}")
                navigateToDetails(dog.id)
            },
            onFavorite = { dog ->
                Log.d(TAG, "Favorite clicked for: ${dog.name}")
                DogRepository.toggleFavorite(dog.id)
                adapter.submit(DogRepository.getAll())
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnFavorites.setOnClickListener {
            Log.d(TAG, "Favorites button clicked")
            navigateToFavorites()
        }

        loadDogs()
    }

    private fun loadDogs() {
        lifecycleScope.launch {
            val success = DogRepository.loadDogs()
            if (success) {
                val dogs = DogRepository.getAll()
                Log.d(TAG, "Loaded ${dogs.size} dogs")
                adapter.submit(dogs)
            } else {
                Log.e(TAG, "Failed to load dogs")
            }
        }
    }

    private fun navigateToDetails(dogId: String) {
        try {
            val action = MainFragmentDirections.actionMainToDetails()
            val bundle = Bundle().apply {
                putString("dogId", dogId)
            }
            findNavController().navigate(R.id.action_main_to_details, bundle)
            Log.d(TAG, "Navigated to details with dogId: $dogId")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation error: ${e.message}", e)
        }
    }

    private fun navigateToFavorites() {
        try {
            findNavController().navigate(R.id.action_main_to_favorites)
            Log.d(TAG, "Navigated to favorites")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation to favorites error: ${e.message}", e)
        }
    }
}