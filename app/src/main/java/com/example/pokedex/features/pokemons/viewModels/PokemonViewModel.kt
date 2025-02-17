package com.example.pokedex.features.pokemons.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.features.pokemons.data.PokemonDetailRequest
import com.example.pokedex.features.pokemons.data.PokemonDetailResponse
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.PokemonPaginatedResponse
import com.example.pokedex.features.pokemons.data.PokemonRepository
import com.example.pokedex.features.pokemons.data.Response
import com.example.pokedex.features.pokemons.useCases.DoGetPokemonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel
@Inject constructor(
    private val pokemonRepository: PokemonRepository,

    private val doGetpokemonList: DoGetPokemonList,
): ViewModel(){
    private val _pokemonList = MutableLiveData<Response<PokemonPaginatedResponse>>(Response.Loading)
    val pokemonListState: LiveData<Response<PokemonPaginatedResponse>> = _pokemonList

    private val _pokemonDetail = MutableLiveData<Response<PokemonDetailResponse>>(Response.Loading)
    val pokemonDetailState: LiveData<Response<PokemonDetailResponse>> = _pokemonDetail

    fun getPokemonsCount(): Int {
        return pokemonRepository.getPokemonsCount()
    }



    fun getPokemons(id: PokemonListRequest) =
        doGetpokemonList(DoGetPokemonList.Params(id)) { useCase ->
            viewModelScope.launch {
                useCase.collect{ response ->
                    _pokemonList.value = response
                }
            }
        }






}