package com.example.dogapp.data.model

data class ApiResponse(
    val data: List<ApiBreed>
)

data class ApiBreed(
    val id: String,
    val attributes: BreedAttributes
)

data class BreedAttributes(
    val name: String,
    val description: String?
)
