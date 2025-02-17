package com.example.pokedex.features.pokemons.data

import com.google.gson.annotations.SerializedName

data class PokemonPaginatedResponse(
    val count:Int?=null,
    val next:String? = null,
    val previous:String? = null,
    var results : List<Pokemon>?=null
)

data class Pokemon(
    val id :Int? = null,
    val name : String? = null,
    val url  : String? = null,
    val sprites : ImagePokemon? = null
)

data class ImagePokemon(
    @SerializedName("front_default")
    val image:String? = null
)