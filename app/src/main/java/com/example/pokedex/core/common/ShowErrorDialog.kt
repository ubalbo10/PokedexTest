package com.example.pokedex.core.common

import android.app.AlertDialog
import android.content.Context

fun showErrorDialog(context: Context, errorMessage: String) {
    // Crear el diálogo de alerta
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Error")
    builder.setMessage(errorMessage) // Mensaje de error
    builder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss() // Cerrar el diálogo cuando el usuario haga clic en OK
    }

    // Mostrar el diálogo
    builder.create().show()
}