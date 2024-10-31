package com.example.equipotres.api

import com.example.equipotres.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("pokedex.json")
    suspend fun getPokemons(): Response<PokemonResponse>
}
