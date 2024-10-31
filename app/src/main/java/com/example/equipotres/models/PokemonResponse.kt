package com.example.equipotres.model

data class PokemonResponse(
    val pokemon: List<Pokemon>
)

data class Pokemon(
    val name: String,
    val img: String
)
