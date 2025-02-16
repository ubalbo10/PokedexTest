package com.example.pokedex.features.pokemons.data

data class PokemonPaginatedResponse(
    val count:Int?=null,
    val next:String? = null,
    val previous:String? = null,
    val results : List<Pokemon>?=null
)

data class Pokemon(
    val id :Int? = null,
    val name : String? = null,
    val url  : String? = null,
    val image : String? = null
)