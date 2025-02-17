package com.example.pokedex.features.pokemons.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
    private val notificationChannelId = "pokemon_updates_channel"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater)
        initView()
        requestNotificationPermission()
        responseData()
        return _binding.root
    }

    private fun initView() {
        // Configuración del RecyclerView
        pokemonAdapter = PokemonAdapter()
        startPokemonUpdate()
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                                showNotification("La lista de Pokémon ha sido actualizada.")
                                updateReciclerView(pokemonList)
                            }  // Carga los detalles de cada pokemon

                            // Manejar la siguiente página si existe
                            val nextUrl = response.next
                            if (nextUrl != null) {
                                lifecycleScope.launch {

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startPokemonUpdate() {
        lifecycleScope.launch {
            while (true) {
                // Esperar 30 segundos antes de hacer la actualización
                delay(3000) // 30 segundos

                // Obtener los Pokémon con un nuevo offset basado en la lista actual
                val currentPokemonCount = pokemonAdapter.itemCount
                val request = PokemonListRequest(
                    offset = currentPokemonCount,
                    limit = 10
                )

                // Realizar la actualización de los Pokémon
                pokemonViewModel.getPokemons(request)


            }
        }
    }

    private fun showNotification(message: String) {
        // Crear un canal de notificación (Requerido desde Android 8.0)
        val channel = NotificationChannel(
            notificationChannelId,
            "Pokemon Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal para actualizaciones de Pokémon"
        }

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Crear la notificación
        val notification = android.app.Notification.Builder(requireContext(), notificationChannelId)
            .setContentTitle("Actualización Pokémon")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verifica si ya tenemos el permiso
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Si no tiene el permiso, solicitamos el permiso
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_PERMISSION
                )
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_PERMISSION = 1001
    }

}
