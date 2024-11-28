package com.example.equipotres.model

import com.google.gson.annotations.SerializedName

data class ApiPokeRespo(
    @SerializedName("pokemon")
    val pokemons: MutableList<ApiPoke>
)
