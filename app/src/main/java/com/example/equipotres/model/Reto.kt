package com.example.equipotres.model

import com.google.firebase.Timestamp

data class Reto(
    var id: String = "",
    val description: String = "",
    val userId: String = "",
    var timestamp: Timestamp = Timestamp.now()
)