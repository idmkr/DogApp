package com.example.dogapp.data.remote

import com.example.dogapp.data.model.ApiResponse
import retrofit2.http.GET

interface DogApiService {

    @GET("breeds")
    suspend fun getBreeds(): ApiResponse
}
