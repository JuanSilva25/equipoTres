package com.example.equipotres.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity
data class Reto(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    var  description: String,
    var  iconResId: Int,
    val createdAt: String
)
