package com.example.equipotres.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipotres.model.ApiPoke
import com.example.equipotres.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

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