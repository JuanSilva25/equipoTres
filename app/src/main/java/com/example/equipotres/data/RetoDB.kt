package com.example.equipotres.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.equipotres.models.Reto
import com.example.equipotres.utils.Constants.NAME_BD

@Database(entities = [Reto::class], version = 1, exportSchema = false)
abstract class RetoDB : RoomDatabase() {
    abstract fun retoDao(): RetoDao

    companion object {
        @Volatile
        private var INSTANCE: RetoDB? = null

        fun getDatabase(context: Context): RetoDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RetoDB::class.java,
                    NAME_BD
                ).fallbackToDestructiveMigration() // Utiliza esta opción si hay un cambio de esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
