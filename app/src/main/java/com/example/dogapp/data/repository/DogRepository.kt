package com.example.dogapp.data.repository

import android.util.Log
import com.example.dogapp.data.model.Dog
import com.example.dogapp.data.remote.DogApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DogRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val api: DogApiService = Retrofit.Builder()
        .baseUrl("https://dogapi.dog/api/v2/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DogApiService::class.java)

    private val dogs = mutableListOf<Dog>()
    private var isLoaded = false

    suspend fun loadDogs(): Boolean {
        if (isLoaded) return true

        return try {
            Log.d("DogRepository", "Loading dogs from API...")

            val response = api.getBreeds()
            dogs.clear()

            response.data.forEachIndexed { index, apiBreed ->
                dogs.add(
                    Dog(
                        id = apiBreed.id,
                        name = apiBreed.attributes.name,
                        description = apiBreed.attributes.description ?: "No description available",
                        imageUrl = getDogImageUrl(index)
                    )
                )
            }

            isLoaded = true
            Log.d("DogRepository", "Successfully loaded ${dogs.size} dogs from API")
            true

        } catch (e: Exception) {
            Log.e("DogRepository", "Failed to load dogs from API: ${e.message}")

            dogs.clear()
            dogs.addAll(getMockDogs())
            isLoaded = true
            Log.d("DogRepository", "Using ${dogs.size} mock dogs")
            true
        }
    }

    private fun getDogImageUrl(index: Int): String {
        val dogImages = listOf(
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
            "https://images.dog.ceo/breeds/retriever-golden/n02099601_100.jpg",
            "https://images.dog.ceo/breeds/germanshepherd/n02106662_100.jpg",
            "https://images.dog.ceo/breeds/bulldog-english/jager-1.jpg",
            "https://images.dog.ceo/breeds/poodle-standard/n02113799_100.jpg",
            "https://images.dog.ceo/breeds/beagle/n02088364_100.jpg",
            "https://images.dog.ceo/breeds/rottweiler/n02106550_100.jpg",
            "https://images.dog.ceo/breeds/dachshund/dog-49392_640.jpg",
            "https://images.dog.ceo/breeds/pomeranian/n02112018_100.jpg",
            "https://images.dog.ceo/breeds/collie-border/n02106166_100.jpg"
        )
        return dogImages[index % dogImages.size]
    }

    private fun getMockDogs(): List<Dog> {
        return listOf(
            Dog(
                id = "1",
                name = "Golden Retriever",
                description = "Friendly, intelligent and devoted. Goldens are excellent family dogs.",
                imageUrl = "https://images.dog.ceo/breeds/retriever-golden/n02099601_100.jpg"
            ),
            Dog(
                id = "2",
                name = "German Shepherd",
                description = "Loyal, confident, courageous and steady. Excellent working dog.",
                imageUrl = "https://images.dog.ceo/breeds/germanshepherd/n02106662_100.jpg"
            ),
            Dog(
                id = "3",
                name = "Bulldog",
                description = "Calm, courageous, and friendly. Bulldogs are dignified but amusing.",
                imageUrl = "https://images.dog.ceo/breeds/bulldog-english/jager-1.jpg"
            )
        )
    }

    fun getAll(): List<Dog> = dogs
    fun getFavorites(): List<Dog> = dogs.filter { it.isFavorite }

    fun toggleFavorite(id: String) {
        dogs.find { it.id == id }?.let {
            it.isFavorite = !it.isFavorite
            Log.d("DogRepository", "Toggled favorite for ${it.name}: ${it.isFavorite}")
        }
    }

    fun getById(id: String): Dog? = dogs.find { it.id == id }
}