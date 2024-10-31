package com.example.equipotres.repository


import com.example.equipotres.utils.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository {
    private val apiService = ApiUtils.getApiService()

    // Función para obtener la URL de una imagen de Pokémon aleatoria
    suspend fun getRandomPokemonImage(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemons()
                if (response.isSuccessful) {
                    val pokemons = response.body()?.pokemon
                    val randomPokemon = pokemons?.shuffled()?.firstOrNull() // Seleccionar un Pokémon aleatorio
                    randomPokemon?.img
                } else {
                    null // En caso de error, devuelve null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
