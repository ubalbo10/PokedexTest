package com.example.pokedex.features.pokemons.data


import com.example.pokedex.features.pokemons.data.mapper.toEntity
import com.example.pokedex.features.pokemons.data.mapper.toPokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject


interface PokemonRepository {

    suspend fun getPokemons(planes: PokemonListRequest): Flow<Response<PokemonPaginatedResponse>>

    //suspend fun getLocalPokemons(): Flow<List<Pokemon>>
    fun getPokemonsCount(): Int
    class Network
    @Inject constructor(
        private val service: PokemonService,
        private val pokemonDao: PokemonDao

    ) : PokemonRepository {
        val pokemonList = mutableListOf<Pokemon>()

        /*
        override suspend fun getLocalPokemons(): Flow<List<Pokemon>> = flow {
            emit(pokemonDao.getAllPokemons()) // Obtener los Pokémon guardados en Room
        }

         */

        override fun getPokemonsCount(): Int {
            return pokemonList.size
        }

        override suspend fun getPokemons(request: PokemonListRequest) =
            flow<Response<PokemonPaginatedResponse>> {
                try {
                    val pokemonResponseList = mutableListOf<Pokemon>()

                    emit(Response.Loading)
                    val response =
                        service.getPokemonList(request.offset, request.limit).awaitResponse()
                    if (response.isSuccessful) {
                        pokemonResponseList.clear()
                        response.body()?.results.let {
                            it?.forEach {
                                val detailResponse =
                                    service.getPokemonDetail(it.url ?: "").awaitResponse()
                                // Aquí manejas la respuesta con los detalles del Pokémon
                                if (detailResponse.isSuccessful) {
                                    val pokemonDetail = detailResponse.body()
                                    pokemonDetail?.let {
                                        pokemonList.add(it)
                                        pokemonResponseList.add(it)
                                    }
                                }
                            }
                            val responsePokemons = response.body()
                            responsePokemons?.results = pokemonResponseList

                            // Guardar los Pokémon en la base de datos local
                            withContext(Dispatchers.IO) {
                                insertPokemons(pokemonList.map { it.toEntity() })
                            }


                            emit(Response.Success(responsePokemons))
                        }
                    } else {
                        when (response.code()) {

                            500 -> emit(
                                Response.Success(
                                    PokemonPaginatedResponse(
                                        count = 0, next = null, previous = null,
                                        results = getAllPokemons().map { it.toPokemon() }
                                    )
                                )
                            )


                            else -> emit(Response.Error("Error no manejado"))
                        }
                    }
                } catch (e: Exception) {
                    //emit(Response.Error(e.message.toString()))
                    e.printStackTrace()


                }
            }


        suspend fun insertPokemons(pokemons: List<PokemonRoom>) {
            pokemons.forEach {
                pokemonDao.insertAll(it)
            }

        }

        suspend fun getAllPokemons(): List<PokemonRoom> {
            return pokemonDao.getAllPokemons()
        }





    }
}


