package com.example.pokedex.features.pokemons.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject


interface PokemonRepository {

    suspend fun getPokemons(planes: PokemonListRequest): Flow<Response<PokemonPaginatedResponse>>

    class Network
    @Inject constructor(
        private val service: PokemonService
    ) : PokemonRepository {



        override suspend fun getPokemons(request: PokemonListRequest)=
            flow<Response<PokemonPaginatedResponse>> {
                try {
                    val pokemonList = mutableListOf<Pokemon>()

                    emit(Response.Loading)
                    val response = service.getPokemonList(request.offset,request.limit).awaitResponse()
                    if (response.isSuccessful) {
                        response.body()?.results.let {
                            it?.forEach {
                                val detailResponse = service.getPokemonDetail(it.url?:"").awaitResponse()
                                // Aquí manejas la respuesta con los detalles del Pokémon
                                if (detailResponse.isSuccessful) {
                                    val pokemonDetail = detailResponse.body()
                                    pokemonDetail?.let {
                                        pokemonList.add(it)
                                    }
                                }
                            }
                            val responsePokemons =response.body()
                            responsePokemons?.results = pokemonList


                            emit(Response.Success(responsePokemons))
                        }
                    } else {
                        when (response.code()) {

                            500 -> emit(
                                Response.Error(
                                    "Error de servidor",
                                )
                            )
                            else -> emit(Response.Error("Error no manejado"))
                        }
                    }
                }  catch (e: Exception) {
                    //emit(Response.Error(e.message.toString()))
                    e.printStackTrace()
                }
            }



        }
    }


