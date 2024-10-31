package com.example.equipotres.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.equipotres.data.repository.RetosRepository
import com.example.equipotres.repository.PokemonRepository

class RetoViewModelFactory(
    private val pokemonRepository: PokemonRepository,
    private val retosRepository: RetosRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetoViewModel::class.java)) {
            return RetoViewModel(pokemonRepository, retosRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
