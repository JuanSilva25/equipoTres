package com.example.equipotres.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.equipotres.R
import com.example.equipotres.databinding.FragmentRetoDialogBinding
import com.example.equipotres.data.RetoViewModelFactory
import com.example.equipotres.repository.PokemonRepository
import com.example.equipotres.data.RetoViewModel
import com.example.equipotres.data.repository.RetosRepository // Asegúrate de importar este repositorio

class RetoDialogFragment : DialogFragment() {

    private var _binding: FragmentRetoDialogBinding? = null
    private val binding get() = _binding!!

    var onDismissListener: (() -> Unit)? = null

    // Instancia del repositorio (considera inicializarlo adecuadamente si requiere parámetros)
    private val pokemonRepository = PokemonRepository() // Asegúrate de que este constructor sea válido
    private lateinit  var retosRepository : RetosRepository// Asegúrate de que este repositorio exista

    // ViewModel
    private val retoViewModel: RetoViewModel by activityViewModels {
        RetoViewModelFactory(pokemonRepository, retosRepository) // Asegúrate de pasar ambos repositorios
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRetoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observación de LiveData
        retoViewModel.pokemonImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            imageUrl?.let {
                Glide.with(this)
                    .load(it)
                    .into(binding.imgPokemon)
            }
        }

        // Observa el LiveData para cerrar el diálogo
        retoViewModel.closeDialog.observe(viewLifecycleOwner) { shouldClose ->
            if (shouldClose == true) {
                dismiss() // Cierra el diálogo
                //retoViewModel.closeDialog.value = false // Reinicia el estado
            }
        }

        // Observación de la descripción del reto
        retoViewModel.retoDescription.observe(viewLifecycleOwner) { description ->
            binding.txtReto.text = description ?: "Reto" // Configura el texto del reto
        }

        binding.btnClose.setOnClickListener {
            retoViewModel.closeDialog() // Usa el LiveData para cerrar el diálogo
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
