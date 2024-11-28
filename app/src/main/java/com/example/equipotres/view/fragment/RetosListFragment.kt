package com.example.equipotres.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipotres.view.adapter.RetoAdapter
import com.example.equipotres.repository.RetosRepository
import com.example.equipotres.databinding.DialogRetoBinding
import com.example.equipotres.databinding.FragmentRetoslistBinding
import com.example.equipotres.model.Reto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RetosListFragment : Fragment() {

    private lateinit var binding: FragmentRetoslistBinding
    @Inject
    lateinit var retosRepository: RetosRepository
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
        //retosRepository = RetosRepository() // Instanciamos el nuevo repositorio de Firestore
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
                val newReto = Reto(
                    description = description,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                    timestamp = Timestamp.now()
                )
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
        lifecycleScope.launch {
            try {
                retosRepository.agregarReto(reto)
                retoAdapter.addReto(reto)
                binding.recyclerViewRetos.smoothScrollToPosition(0)
                Toast.makeText(requireContext(), "Reto agregado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al agregar el reto", Toast.LENGTH_SHORT).show()
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
        lifecycleScope.launch {
            try {
                retosRepository.actualizarReto(reto)
                retoAdapter.updateReto(reto)
                Toast.makeText(requireContext(), "Reto actualizado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al actualizar el reto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteReto(reto: Reto) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este reto?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        retosRepository.eliminarReto(reto.id)
                        retoAdapter.deleteReto(reto)
                        Toast.makeText(requireContext(), "Reto eliminado", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al eliminar el reto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun loadRetos() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RetosListFragment", "Cargando retos para userId: $userId")

        lifecycleScope.launch {
            try {
                retosRepository.obtenerListaRetos(userId).collect { retosList ->
                    if (retosList.isEmpty()) {
                        Log.d("RetosListFragment", "No se encontraron retos para este usuario.")
                    } else {
                        Log.d("RetosListFragment", "Retos encontrados: ${retosList.size}")
                    }
                    retoAdapter.updateRetos(retosList)
                }
            } catch (e: Exception) {
                Log.e("RetosListFragment", "Error al cargar retos: ${e.message}")
                Toast.makeText(requireContext(), "Error al cargar retos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}