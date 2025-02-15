package com.example.pokedex.features.pokemons.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject


interface PokemonRepository {

    suspend fun getPokemons(planes: PokemonListRequest): Flow<Response<List<PokemonResponse>>>
    suspend fun getPokemonDetail(planes: PokemonDetailRequest): Flow<Response<PokemonDetailResponse>>

    class Network
    @Inject constructor(
        private val service: PokemonService
    ) : PokemonRepository {



        override suspend fun getPokemons(planes: PokemonListRequest)=
            flow<Response<List<PokemonResponse>>> {
                try {
                    emit(Response.Loading)
                    val response = service.getPokemonList(planes.id?.toInt()?:0).awaitResponse()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emit(Response.Success(it))
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



        override suspend fun getPokemonDetail(planes: PokemonDetailRequest) =
            flow<Response<PokemonDetailResponse>> {
                try {
                    emit(Response.Loading)
                    val response = service.getPokemonDetail(planes.id?.toInt()?:0).awaitResponse()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emit(Response.Success(it))
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


