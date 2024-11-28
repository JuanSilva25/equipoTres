package com.example.equipotres.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equipotres.model.ApiPoke
import com.example.equipotres.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonsViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val pokemonRepository = PokemonRepository(context)

    private val _pokemonsList = MutableLiveData<MutableList<ApiPoke>>()
    val pokemonsList: LiveData<MutableList<ApiPoke>> get() = _pokemonsList

    fun getPokemons() {
        viewModelScope.launch {
            try {
                _pokemonsList.value = pokemonRepository.getPokemons()

            } catch (e: Exception) {
            }
        }
    }
}