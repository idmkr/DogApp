package com.example.dogapp.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.data.repository.DogRepository
import com.example.dogapp.databinding.FragmentDetailsBinding

class   DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailsBinding.bind(view)

        val dogId = arguments?.getString("dogId") ?: return

        val dog = DogRepository.getById(dogId) ?: return

        binding.textTitle.text = dog.name
        binding.textDescription.text = dog.description

        Glide.with(this)
            .load(dog.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_dialog_alert)
            .into(binding.imageDog)
    }
}