package com.example.equipotres.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipotres.database.RetoDatabaseHelper
import com.example.equipotres.models.Reto
import com.example.equipotres.repository.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RetoViewModel(context: Context) : ViewModel() {

    private val repository = PokemonRepository()
    private val databaseHelper = RetoDatabaseHelper.getInstance(context)

    private val _pokemonImageUrl = MutableLiveData<String>()
    val pokemonImageUrl: LiveData<String> get() = _pokemonImageUrl

    private val _reto = MutableLiveData<Reto>()
    val reto: LiveData<Reto> get() = _reto

    init {
        fetchRandomPokemon()
        fetchRandomReto()
    }

    private fun fetchRandomPokemon() {
        viewModelScope.launch {
            val imageUrl = repository.getRandomPokemonImage()
            Log.d("ssss", imageUrl.toString());

            _pokemonImageUrl.postValue(imageUrl.toString())
        }
    }

    private fun fetchRandomReto() {
        CoroutineScope(Dispatchers.IO).launch {
            val retos = databaseHelper.getAllRetos()
            if (retos.isNotEmpty()) {
                val randomReto = retos.random()
                _reto.postValue(randomReto)
            }
        }
    }
}
