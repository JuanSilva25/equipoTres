package com.example.equipotres.repository

import android.content.Context
import android.util.Log
import com.example.equipotres.model.ApiPoke
import com.example.equipotres.webservice.ApiService
import com.example.equipotres.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPokemons(): MutableList<ApiPoke> {
        return withContext(Dispatchers.IO) {
            try {
                // Log para indicar que se está haciendo la solicitud
                Log.d("PokemonRepository", "Iniciando solicitud a la API de Pokémon")

                val response = apiService.getPokemons()
                val pokemonList = response.pokemons

                // Log para mostrar los datos obtenidos
                Log.d("PokemonRepository", "Respuesta de la API: ${pokemonList.size} Pokémon obtenidos")

                pokemonList
            } catch (e: Exception) {
                // Log de error
                Log.e("PokemonRepository", "Error al obtener los Pokémon: ${e.message}", e)
                mutableListOf() // Retorna lista vacía si falla
            }
        }
    }

}