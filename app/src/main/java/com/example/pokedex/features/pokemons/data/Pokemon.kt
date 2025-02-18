package com.example.pokedex.features.pokemons.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonRoom(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("name")val name: String,
    @ColumnInfo("url")val url: String,

    )
