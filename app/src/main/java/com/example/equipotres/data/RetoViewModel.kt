package com.example.equipotres.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipotres.data.repository.RetosRepository
import com.example.equipotres.models.Reto
import com.example.equipotres.repository.PokemonRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RetoViewModel(private val repository: PokemonRepository, private val repositoryReto: RetosRepository) : ViewModel() {

    private val _pokemonImageUrl = MutableLiveData<String?>()
    val pokemonImageUrl: LiveData<String?> get() = _pokemonImageUrl

    private val _retoDescription = MutableLiveData<String?>()
    val retoDescription: LiveData<String?> get() = _retoDescription

    private val _closeDialog = MutableLiveData<Boolean>()
    val closeDialog: LiveData<Boolean> get() = _closeDialog

    init {
        loadPokemonImage()
        loadReto() // Cargar el reto aleatorio al inicializar el ViewModel
    }

    private fun loadPokemonImage() {
        viewModelScope.launch {
            val imageUrl = repository.getRandomPokemonImage()
            _pokemonImageUrl.value = imageUrl
        }
    }

    private fun loadReto() {
        viewModelScope.launch {
            repositoryReto.obtenerRetoAleatorio().collect { reto ->
                _retoDescription.value = reto?.description // Asignar solo la descripción al LiveData
            }
        }
    }

    fun closeDialog() {
        _closeDialog.value = true
    }
}
