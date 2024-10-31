package com.example.equipotres.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.equipotres.api.ApiService // Importa ApiService

object ApiUtils {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // Usar la constante de Constants.kt
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
