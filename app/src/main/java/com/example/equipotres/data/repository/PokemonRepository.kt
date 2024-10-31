package com.example.equipotres.repository

import com.example.equipotres.api.ApiService
import com.example.equipotres.utils.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PokemonRepository {
    private val apiService = ApiUtils.getApiService()

    // Función para obtener la URL de una imagen de Pokémon aleatoria
    suspend fun getRandomPokemonImage(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemons()
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body()?.string() ?: "")
                    val pokemons = jsonResponse.getJSONArray("pokemon")
                    val randomPokemon = pokemons.getJSONObject((0 until pokemons.length()).random())
                    randomPokemon.getString("img") // Extraemos directamente la URL de la imagen
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
