package com.example.equipotres.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipotres.models.Reto
import com.example.equipotres.repository.PokemonRepository
import kotlinx.coroutines.launch

class RetoViewModel(private val repository: PokemonRepository) : ViewModel() {


    private val _reto = MutableLiveData<Reto>()
    val reto: LiveData<Reto> get() = _reto

    private val _pokemonImageUrl = MutableLiveData<String>()
    val pokemonImageUrl: LiveData<String> get() = _pokemonImageUrl

    init {
        loadReto()
        loadPokemonImage()
    }

    private fun loadReto() {
        _reto.value = Reto("Realiza un reto")
    }

    private fun loadPokemonImage() {
        viewModelScope.launch {
            val imageUrl = repository.getRandomPokemonImage()
            _pokemonImageUrl.postValue(imageUrl)
        }
    }
}
