package com.example.pokedex.features.pokemons.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.core.common.showErrorDialog
import com.example.pokedex.databinding.FragmentPokemonListBinding
import com.example.pokedex.features.pokemons.data.Pokemon
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.PokemonRepository
import com.example.pokedex.features.pokemons.data.Response
import com.example.pokedex.features.pokemons.viewModels.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.livinapp.lealtad.core.common.BaseFragment

@AndroidEntryPoint
class ListPokemonFragment : BaseFragment() {
    private lateinit var _binding: FragmentPokemonListBinding
    private val pokemonViewModel by activityViewModels<PokemonViewModel>()
    private lateinit var pokemonAdapter: PokemonAdapter
    private val pokemonUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Aquí recibimos el broadcast y le indicamos al ViewModel que se actualicen los Pokémon
            pokemonViewModel.getPokemons(PokemonListRequest(pokemonViewModel.getPokemonsCount(), 15)) // O pasa los parámetros correspondientes
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater)
        initView()
        responseData()
        // Configura el receptor del broadcast
        val filter = IntentFilter("com.example.pokedex.POKEMON_UPDATED")
        ContextCompat.registerReceiver(
            requireActivity(),
            pokemonUpdateReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
        return _binding.root
    }

    private fun initView() {
        // Configuración del RecyclerView
        pokemonAdapter = PokemonAdapter()
        _binding.recyclerViewPokemon.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pokemonAdapter
        }

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
                            }

                        }
                    }

                    is Response.Error -> {
                        hideLoadingDialog()
                        showErrorDialog(requireContext(),"No se pudo obtener una respuesta del servidor...")
                    }
                }
            }

        }
    }

    private fun updateReciclerView(pokemons : List<Pokemon>){
        pokemonAdapter.addPokemons(pokemons)

    }
}
