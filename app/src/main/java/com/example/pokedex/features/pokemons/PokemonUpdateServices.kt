package com.example.pokedex.features.pokemons

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pokedex.features.pokemons.data.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonUpdateService : Service() {

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private val notificationChannelId = "pokemon_updates_channel"

    // El scope para ejecutar las corutinas en segundo plano
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null  // Este servicio no se vincula, solo se inicia
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Iniciar la actualización periódica de Pokémon
        serviceScope.launch {
            while (true) {
                showNotification("La lista de Pokémon ha sido actualizada.")

                // Enviar un Broadcast cuando los Pokémon se actualicen
                val updateIntent = Intent("com.example.pokedex.POKEMON_UPDATED")
                sendBroadcast(updateIntent)
                delay(30 * 1000L)
            }
        }
        return START_STICKY  // El servicio sigue corriendo incluso si es destruido por el sistema
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()  // Cancelar el scope cuando el servicio se destruya
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

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Crear la notificación
        val notification = android.app.Notification.Builder(applicationContext, notificationChannelId)
            .setContentTitle("Actualización Pokémon")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(1, notification)
    }


}
