package com.example.equipotres.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.equipotres.databinding.FragmentDialogBinding

class DialogCustom {
    companion object {
        fun showDialogCustom(
            context: Context
        ) {
            val inflater = LayoutInflater.from(context)
            val binding = FragmentDialogBinding.inflate(inflater)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false) //para cuando se cliquee por los lados no se pueda
            alertDialog.setView(binding.root) //establecer la vista de un cuadro de dialogo


            binding.btnCerrar.setOnClickListener {
                Toast.makeText(context,"Cerrar", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
            alertDialog.show()

        }
    }
}