package com.example.equipotres.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.equipotres.databinding.FragmentDialogBinding

class DialogCustom {
    companion object {
        fun showDialogCustom(
            context: Context,
            challengeDescription: String // Agregar el parámetro para la descripción
        ) {
            val inflater = LayoutInflater.from(context)
            val binding = FragmentDialogBinding.inflate(inflater)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false) // Para que no se cierre al hacer clic fuera
            alertDialog.setView(binding.root) // Establecer la vista personalizada del diálogo

            // Configurar el texto del reto aleatorio
            binding.tvChallenge.text = challengeDescription

            // Acción del botón cerrar
            binding.btnCerrar.setOnClickListener {
                Toast.makeText(context, "Cerrar", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}
