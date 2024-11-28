package com.example.equipotres.model

import com.google.gson.annotations.SerializedName

data class ApiPoke(
    @SerializedName("id")
    val id: Int,

    @SerializedName("img")
    val img: String
)
