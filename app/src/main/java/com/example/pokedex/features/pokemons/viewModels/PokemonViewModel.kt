package com.example.pokedex.features.pokemons.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.features.pokemons.data.PokemonDetailResponse
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.PokemonResponse
import com.example.pokedex.features.pokemons.data.Response
import com.example.pokedex.features.pokemons.useCases.DoGetPokemonDetail
import com.example.pokedex.features.pokemons.useCases.DoGetPokemonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel
@Inject constructor(
    private val doGetpokemonList: DoGetPokemonList,
    private val doGetpokemonDetail: DoGetPokemonDetail
): ViewModel(){
    private val _pokemonList = MutableLiveData<Response<List<PokemonResponse>>>(Response.Loading)
    val pokemonListState: LiveData<Response<List<PokemonResponse>>> = _pokemonList

    private val _pokemonDetail = MutableLiveData<Response<PokemonDetailResponse>>(Response.Loading)
    val pokemonDetailState: LiveData<Response<PokemonDetailResponse>> = _pokemonDetail



    fun getPokemons(pokemonListRequest: PokemonListRequest) =
        doGetpokemonList(DoGetPokemonList.Params(pokemonListRequest)) { useCase ->
            viewModelScope.launch {
                useCase.collect{ response ->
                    award.value = response
                }
            }
        }

    fun getFilter(plan: Int) =
        doGetFilter(DoGetFilter.Params(plan)) { useCase ->
            viewModelScope.launch {
                useCase.collect{ response ->
                    filter.value = response
                }
            }
        }

}