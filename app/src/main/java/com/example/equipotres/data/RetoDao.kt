package com.example.equipotres.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.example.equipotres.models.Reto
import kotlinx.coroutines.flow.Flow

@Dao
interface RetoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReto(reto: Reto): Long

    @Query("SELECT * FROM retos ORDER BY id DESC")
    fun getRetosList(): Flow<List<Reto>>

    @Query("SELECT * FROM retos ORDER BY RANDOM() LIMIT 1")
    fun getRandomReto(): Flow<Reto?>

    @Query("SELECT * FROM retos WHERE id = :retoId LIMIT 1")
    suspend fun getRetoById(retoId: Int): Reto?

    @Query("UPDATE retos SET description = :nuevaDescripcion WHERE id = :retoId")
    suspend fun updateRetoDescription(retoId: Int, nuevaDescripcion: String): Int

    @Update
    suspend fun updateReto(reto: Reto): Int

    @Query("DELETE FROM retos WHERE id = :retoId")
    suspend fun deleteReto(retoId: Int): Int
}
