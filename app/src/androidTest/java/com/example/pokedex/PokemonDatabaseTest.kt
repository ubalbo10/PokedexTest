package com.example.pokedex

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.features.pokemons.data.PokemonDao
import com.example.pokedex.features.pokemons.data.PokemonDatabase
import com.example.pokedex.features.pokemons.data.PokemonRoom
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonDatabaseTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var pokemonDatabase: PokemonDatabase

    @Before
    fun setUp() {
        // Crear una base de datos en memoria para pruebas
        pokemonDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).build()
        pokemonDao = pokemonDatabase.pokemonDao()
    }

    @Test
    fun testInsertAndGetPokemon() = runBlocking {
        val pokemon = PokemonRoom(id = 1, name = "Bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
        val p = pokemonDao.insertAll(pokemon)

        // Comprobar si se ha insertado correctamente
        //val retrievedPokemon = pokemonDao.getPokemonById(1)
        assertThat(pokemonDao.getAllPokemons().first().name, `is`("Bulbasaur"))
    }

    @After
    fun tearDown() {
        pokemonDatabase.close()
    }
}
