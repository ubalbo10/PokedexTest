package com.example.pokedex.features.pokemons.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentPokemonListBinding
import com.example.pokedex.features.pokemons.data.Pokemon
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.Response
import com.example.pokedex.features.pokemons.viewModels.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            limit = 15
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
                                updateReciclerView(pokemonList)
                            }  // Carga los detalles de cada pokemon

                            // Manejar la siguiente página si existe
                            val nextUrl = response.next
                            if (nextUrl != null) {
                                lifecycleScope.launch {
                                    delay(5000)
                                    // Si hay más pokemones, haz una llamada para obtenerlos

                                    loadNextPage(nextUrl)
                                }

                            }
                        }
                    }

                    is Response.Error -> {
                        hideLoadingDialog()
                        // Mostrar mensaje de error
                    }
                }
            }

        }
    }

    private fun updateReciclerView(pokemons : List<Pokemon>){
        pokemonAdapter.addPokemons(pokemons)

    }

    private fun loadNextPage(url: String) {
        val request = parseNextUrl(url)
        // Realiza la llamada para obtener la siguiente página de pokemones
        if (request != null) {
            pokemonViewModel.getPokemons(
                request
            )
        }
    }
    fun parseNextUrl(nextUrl: String): PokemonListRequest? {
        val uri = Uri.parse(nextUrl) // Usa Uri para analizar la URL
        val offset = uri.getQueryParameter("offset")?.toIntOrNull()
        val limit = uri.getQueryParameter("limit")?.toIntOrNull()

        return if (offset != null && limit != null) {
            PokemonListRequest(offset, limit)
        } else {
            null // Devuelve null si no se pueden extraer los valores
        }
    }

}
