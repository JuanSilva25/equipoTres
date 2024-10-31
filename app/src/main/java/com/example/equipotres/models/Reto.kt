package com.example.equipotres.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "retos")
data class Reto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String = getCurrentDate()
) {
    companion object {
        fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}
