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

    @Query("SELECT * FROM Reto")
    fun getRetosList(): Flow<List<Reto>>

    @Query("SELECT * FROM Reto ORDER BY RANDOM() LIMIT 1")
    fun getRandomReto(): Flow<Reto?>

    @Update
    suspend fun updateReto(reto: Reto): Int

    @Query("UPDATE Reto SET description = :newDescription WHERE id = :retoId")
    suspend fun updateRetoDescription(retoId: Int, newDescription: String): Int

    @Query("SELECT * FROM Reto WHERE id = :retoId")
    suspend fun getRetoById(retoId: Int): Reto?

    @Query("DELETE FROM Reto WHERE id = :retoId")
    suspend fun deleteReto(retoId: Int): Int
}

