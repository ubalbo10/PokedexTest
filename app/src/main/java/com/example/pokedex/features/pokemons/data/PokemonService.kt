package com.example.pokedex.features.pokemons.data

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonService
@Inject constructor(retrofit: Retrofit) : PokemonApi{
    private val pokemonApi by lazy { retrofit.create(PokemonApi::class.java) }
    override fun getPokemonList(id: Int): Call<List<PokemonResponse>> = pokemonApi.getPokemonList(id)

    override fun getPokemonDetail(plan: Int): Call<PokemonDetailResponse> = pokemonApi.getPokemonDetail(plan)


}