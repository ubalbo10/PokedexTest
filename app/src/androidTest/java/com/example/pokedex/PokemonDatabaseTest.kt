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
        pokemonDao.insertAll(pokemon)

        // Comprobar si se ha insertado correctamente
        //val retrievedPokemon = pokemonDao.getPokemonById(1)
        assertThat(pokemonDao.getAllPokemons().first().name, `is`("Bulbasaur"))
    }

    @Test
    fun testInsertAndGetAllPokemons() = runBlocking {
        // Crear una lista de pokémones para insertar
        val pokemonList = listOf(
            PokemonRoom(name = "Bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonRoom(name = "Charmander", url = "https://pokeapi.co/api/v2/pokemon/4/"),
            PokemonRoom(name = "Squirtle", url = "https://pokeapi.co/api/v2/pokemon/7/")
        )

        // Insertar los pokémones en la base de datos
        pokemonDao.insertAll(*pokemonList.toTypedArray())

        // Recuperar los pokémones desde la base de datos
        val retrievedPokemons = pokemonDao.getAllPokemons()

        // Verificar que el tamaño de la lista sea el correcto
        assertThat(retrievedPokemons.size, `is`(pokemonList.size))

        // Verificar que los datos insertados sean los mismos que los recuperados
        for (i in pokemonList.indices) {
            assertThat(retrievedPokemons[i].name, `is`(pokemonList[i].name))
            assertThat(retrievedPokemons[i].url, `is`(pokemonList[i].url))
        }
    }

    @After
    fun tearDown() {
        pokemonDatabase.close()
    }
}
