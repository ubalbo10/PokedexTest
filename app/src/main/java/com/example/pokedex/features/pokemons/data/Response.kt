package com.example.pokedex.features.pokemons.data

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Response<T>()

    data class Error(
        val message: String,
        val exception: Exception? = null
    ): Response<Nothing>()
}
