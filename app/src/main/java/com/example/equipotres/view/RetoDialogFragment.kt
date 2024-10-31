package com.example.equipotres.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.equipotres.databinding.FragmentRetoDialogBinding
import com.example.equipotres.R
import com.example.equipotres.viewmodel.RetoViewModel
import com.example.equipotres.viewmodel.RetoViewModelFactory

class RetoDialogFragment : DialogFragment() {

    private var _binding: FragmentRetoDialogBinding? = null
    private val binding get() = _binding!!

    var onDismissListener: (() -> Unit)? = null
    private lateinit var viewModel: RetoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, RetoViewModelFactory(requireActivity())).get(RetoViewModel::class.java)
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

        viewModel.reto.observe(viewLifecycleOwner, { reto ->
            binding.txtReto.text = reto.description
        })

        viewModel.pokemonImageUrl.observe(viewLifecycleOwner, { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imgPokemon)
        })

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
