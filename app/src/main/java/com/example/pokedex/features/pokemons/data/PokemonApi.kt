package com.example.pokedex.features.pokemons.data


import net.livinapp.lealtad.core.api.ApiConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonApi {
    companion object {
        private const val POKEMONS = "pokemon"
        private const val DETAIL_POKEMON = "filtros/{plan}"
    }

    @Headers(ApiConfig.CONTENT_TYPE_JSON)
    @GET(POKEMONS)
    fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit")  limit : Int
    )
            : Call<PokemonPaginatedResponse>

    @Headers(ApiConfig.CONTENT_TYPE_JSON)
    @GET
    fun getPokemonDetail(@Url url: String): Call<Pokemon>

}