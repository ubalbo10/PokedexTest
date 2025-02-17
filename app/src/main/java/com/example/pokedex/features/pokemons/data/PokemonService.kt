package com.example.pokedex.features.pokemons.data

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonService
@Inject constructor(retrofit: Retrofit) : PokemonApi{
    private val pokemonApi by lazy { retrofit.create(PokemonApi::class.java) }
    override fun getPokemonList(offset : Int, limit:Int): Call<PokemonPaginatedResponse> = pokemonApi.getPokemonList(offset,limit)

    override fun getPokemonDetail(url: String): Call<Pokemon> = pokemonApi.getPokemonDetail(url)


}