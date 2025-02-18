package com.example.pokedex.features.pokemons.data.mapper

import com.example.pokedex.features.pokemons.data.Pokemon
import com.example.pokedex.features.pokemons.data.PokemonRoom

fun Pokemon.toEntity(): PokemonRoom {
    return PokemonRoom(
        name = this.name?:"",
        url = this.url?:""
    )
}

fun PokemonRoom.toPokemon() : Pokemon {
    return Pokemon(
        name = this.name,
        url =  this.url
    )
}