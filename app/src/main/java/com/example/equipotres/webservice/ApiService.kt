package com.example.equipotres.webservice

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("pokedex.json")
    suspend fun getPokemons(): Response<ResponseBody>
}
