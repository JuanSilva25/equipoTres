package com.example.equipotres.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipotres.adapter.RetoAdapter
import com.example.equipotres.data.repository.RetosRepository
import com.example.equipotres.databinding.DialogRetoBinding
import com.example.equipotres.databinding.FragmentRetoslistBinding
import com.example.equipotres.models.Reto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetosListFragment : Fragment() {

    private lateinit var binding: FragmentRetoslistBinding
    private lateinit var retosRepository: RetosRepository
    private lateinit var retoAdapter: RetoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRetoslistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retosRepository = RetosRepository(requireContext())
        retoAdapter = RetoAdapter(mutableListOf(), ::editReto, ::deleteReto)
        setupRecyclerView()
        loadRetos()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRetos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = retoAdapter
        }
    }

    private fun setupListeners() {
        binding.arrowLeft.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddReto.setOnClickListener {
            showAddRetoDialog()
        }
    }

    private fun showAddRetoDialog() {
        val dialogBinding = DialogRetoBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.saveButton.setOnClickListener {
            val description = dialogBinding.dialogDescription.text.toString().trim()
            if (description.isNotEmpty()) {
                val newReto = Reto(description = description)
                saveReto(newReto)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveReto(reto: Reto) {
        CoroutineScope(Dispatchers.IO).launch {
            retosRepository.agregarReto(reto)
            withContext(Dispatchers.Main) {
                retoAdapter.addReto(reto)
                binding.recyclerViewRetos.smoothScrollToPosition(0)
                Toast.makeText(requireContext(), "Reto agregado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editReto(reto: Reto) {
        val dialogBinding = DialogRetoBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.dialogDescription.setText(reto.description)

        dialogBinding.saveButton.setOnClickListener {
            val description = dialogBinding.dialogDescription.text.toString().trim()
            if (description.isNotEmpty()) {
                val updatedReto = reto.copy(description = description)
                updateReto(updatedReto)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateReto(reto: Reto) {
        CoroutineScope(Dispatchers.IO).launch {
            val rowsUpdated = retosRepository.actualizarReto(reto)
            withContext(Dispatchers.Main) {
                if (rowsUpdated > 0) {
                    retoAdapter.updateReto(reto)
                    Toast.makeText(requireContext(), "Reto actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el reto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteReto(reto: Reto) {
        CoroutineScope(Dispatchers.IO).launch {
            retosRepository.eliminarReto(reto.id)
            withContext(Dispatchers.Main) {
                retoAdapter.deleteReto(reto)
                Toast.makeText(requireContext(), "Reto eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadRetos() {
        CoroutineScope(Dispatchers.IO).launch {
            retosRepository.obtenerListaRetos().collect { retosList ->
                withContext(Dispatchers.Main) {
                    retoAdapter.updateRetos(retosList)
                }
            }
        }
    }
}