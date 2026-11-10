package com.example.dogapp.data.model

data class Dog(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    var isFavorite: Boolean = false
)
