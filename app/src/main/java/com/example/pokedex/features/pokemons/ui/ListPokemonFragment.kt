package com.example.pokedex.features.pokemons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentPokemonListBinding
import com.example.pokedex.features.pokemons.data.Pokemon
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.Response
import com.example.pokedex.features.pokemons.viewModels.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.livinapp.lealtad.core.common.BaseFragment

@AndroidEntryPoint
class ListPokemonFragment : BaseFragment() {
    private lateinit var _binding: FragmentPokemonListBinding
    private val pokemonViewModel by viewModels<PokemonViewModel>()
    private lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater)
        initView()
        responseData()
        return _binding.root
    }

    private fun initView() {
        // Configuración del RecyclerView
        pokemonAdapter = PokemonAdapter()
        _binding.recyclerViewPokemon.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pokemonAdapter
        }

        // Hacer la petición para obtener los Pokémon
        val request = PokemonListRequest(
            offset = 0,
            limit = 20
        )
        pokemonViewModel.getPokemons(request)
    }

    private fun responseData() {
        with(pokemonViewModel) {
            // Observar la respuesta de la lista de Pokémon
            pokemonListState.observe(viewLifecycleOwner) { resp ->
                when (resp) {
                    is Response.Loading -> {
                        showLoadingDialog()
                    }


                    is Response.Success -> {
                        hideLoadingDialog()
                        val response = resp.data
                        if (response != null) {
                            // Aquí cargas la lista de pokemones obtenidos
                            val pokemonList = response.results
                            if (pokemonList != null) {
                                loadPokemonDetails(pokemonList)
                            }  // Carga los detalles de cada pokemon

                            // Manejar la siguiente página si existe
                            val nextUrl = response.next
                            if (nextUrl != null) {
                                // Si hay más pokemones, haz una llamada para obtenerlos
                                loadNextPage(nextUrl)
                            }
                        }
                    }

                    is Response.Error -> {
                        hideLoadingDialog()
                        // Mostrar mensaje de error
                    }
                }
            }

            // Observar la respuesta de los detalles de los Pokémon
            pokemonDetailState.observe(viewLifecycleOwner) { resp ->
                when (resp) {
                    is Response.Loading -> {
                        // Mostrar loading
                    }

                    is Response.Success -> {
                        val response = resp.data
                        if (response != null) {
                            // Actualizar la imagen del Pokémon correspondiente
                            pokemonAdapter.updatePokemonImage(response)
                        }
                    }

                    is Response.Error -> {
                        // Mostrar mensaje de error
                    }
                }
            }
        }
    }

    private fun loadPokemonDetails(pokemonList: List<Pokemon>) {
        pokemonList.forEach { pokemon ->
            //pokemonViewModel.getPokemonDetail(pokemon.url)  // Llamar para obtener detalles (incluyendo imagen)
        }
    }

    private fun loadNextPage(url: String) {
        // Realiza la llamada para obtener la siguiente página de pokemones
        //pokemonViewModel.getPokemonList(url)
    }
}
