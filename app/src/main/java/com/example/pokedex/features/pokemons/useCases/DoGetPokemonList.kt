package com.example.pokedex.features.pokemons.useCases

import androidx.annotation.Keep
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.PokemonRepository
import com.example.pokedex.features.pokemons.data.PokemonResponse
import net.livinapp.lealtad.core.interactor.UseCaseFlow
import javax.inject.Inject

class DoGetPokemonList
@Inject constructor(private val pokemonRepository:PokemonRepository )
    : UseCaseFlow<List<PokemonResponse>, DoGetPokemonList.Params>() {

    override suspend fun run(params: Params) = pokemonRepository.getPokemons(params.pokemonListRequest)

    @Keep
    data class Params(val pokemonListRequest: PokemonListRequest)
}