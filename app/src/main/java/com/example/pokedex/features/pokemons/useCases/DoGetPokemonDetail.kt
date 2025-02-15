package com.example.pokedex.features.pokemons.useCases

import androidx.annotation.Keep
import com.example.pokedex.features.pokemons.data.PokemonDetailRequest
import com.example.pokedex.features.pokemons.data.PokemonDetailResponse
import com.example.pokedex.features.pokemons.data.PokemonRepository
import net.livinapp.lealtad.core.interactor.UseCaseFlow
import javax.inject.Inject

class DoGetPokemonDetail
@Inject constructor(private val pokemonRepository:PokemonRepository )
    : UseCaseFlow<PokemonDetailResponse, DoGetPokemonDetail.Params>() {

    override suspend fun run(params: Params) = pokemonRepository.getPokemonDetail(params.pokemonDetailRequest)

    @Keep
    data class Params(val pokemonDetailRequest: PokemonDetailRequest)
}