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

class  RetosListFragment : Fragment() {

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
        retoAdapter = RetoAdapter(mutableListOf())
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
        // Navegación de regreso con la flecha
        binding.arrowLeft.setOnClickListener {
            findNavController().navigateUp()
        }

        // Abrir el diálogo para agregar un nuevo reto
        binding.fabAddReto.setOnClickListener {
            showAddRetoDialog()
        }
    }

    private fun showAddRetoDialog() {
        val dialogBinding = DialogRetoBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        // Configurar el botón "Guardar" para crear el reto
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

        // Configurar el botón "Cancelar" para cerrar el diálogo
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
                binding.recyclerViewRetos.smoothScrollToPosition(retoAdapter.itemCount - 1)
                Toast.makeText(requireContext(), "Reto agregado", Toast.LENGTH_SHORT).show()
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