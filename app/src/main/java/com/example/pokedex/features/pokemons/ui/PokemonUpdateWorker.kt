package com.example.pokedex.features.pokemons.ui

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Worker
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.features.pokemons.data.Pokemon
import com.example.pokedex.features.pokemons.data.PokemonListRequest
import com.example.pokedex.features.pokemons.data.PokemonRepository
import com.example.pokedex.features.pokemons.viewModels.PokemonViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltWorker
class PokemonUpdateWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val pokemonRepository: PokemonRepository
) : CoroutineWorker(appContext, workerParams){

    private val pokemonViewModel: PokemonViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(applicationContext as Application)
            .create(PokemonViewModel::class.java)
    }
    override suspend fun doWork(): Result {
        // Obtener 10 nuevos pokemones
        val currentOffset = pokemonViewModel.getPokemonsCount()

        val request = PokemonListRequest(offset = currentOffset, limit = 10)
        pokemonViewModel.getPokemons(request)


        // Mostrar notificación
        showNotification()

        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "pokemon_update_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Actualización de Pokémon", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Pokémon Actualizados")
            .setContentText("La lista de Pokémon ha sido actualizada.")
            .setSmallIcon(R.drawable.ic_launcher_background) // Asegúrate de tener un ícono en tus recursos
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(1, notification)
    }
}
