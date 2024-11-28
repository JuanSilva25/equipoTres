package com.example.equipotres.webservice

import com.example.equipotres.model.ApiPokeRespo
import com.example.equipotres.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getPokemons(): ApiPokeRespo
}