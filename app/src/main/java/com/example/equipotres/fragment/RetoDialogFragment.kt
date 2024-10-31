package com.example.equipotres.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.equipotres.databinding.RetoDialogFragmentBinding
import com.example.equipotres.R

class RetoDialogFragment : DialogFragment() {

    private var _binding: FragmentRetoDialogBinding? = null
    private val binding get() = _binding!!

    var onDismissListener: (() -> Unit)? = null

    // Propiedades para el reto y la imagen del Pokémon
    var retoDescription: String? = null
    var pokemonImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRetoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el texto del reto y cargar la imagen
        binding.txtReto.text = retoDescription

        pokemonImageUrl?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imgPokemon)
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        isCancelable = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
