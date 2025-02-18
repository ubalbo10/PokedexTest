package com.example.pokedex.features.pokemons.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {


    @Query("SELECT * FROM pokemon")
    fun getAllPokemons(): List<PokemonRoom>

    @Insert
    fun insertAll(vararg pokemons: PokemonRoom)
}
