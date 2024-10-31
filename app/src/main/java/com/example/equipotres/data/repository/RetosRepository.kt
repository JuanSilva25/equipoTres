package com.example.equipotres.data.repository

import android.content.Context
import com.example.equipotres.data.RetoDB
import com.example.equipotres.data.RetoDao
import com.example.equipotres.models.Reto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RetosRepository(context: Context) {

    private val retoDao: RetoDao = RetoDB.getDatabase(context).retoDao()

    // Método para agregar un reto
    suspend fun agregarReto(reto: Reto): Long = withContext(Dispatchers.IO) {
        retoDao.saveReto(reto)
    }

    // Cambiado para devolver Flow<List<Reto>> y adaptarse al tipo de retorno de RetoDao
    fun obtenerListaRetos(): Flow<List<Reto>> = retoDao.getRetosList()

    // Método para obtener un reto aleatorio como Flow
    fun obtenerRetoAleatorio(): Flow<Reto?> = retoDao.getRandomReto()

    // Método para obtener un reto por su ID
    suspend fun obtenerRetoPorId(retoId: Int): Reto? = withContext(Dispatchers.IO) {
        retoDao.getRetoById(retoId)
    }

    // Método para actualizar solo la descripción de un reto
    suspend fun actualizarDescripcionReto(retoId: Int, nuevaDescripcion: String): Int {
        return withContext(Dispatchers.IO) {
            retoDao.updateRetoDescription(retoId, nuevaDescripcion)
        }
    }

    // Método para actualizar un reto completo
    suspend fun actualizarReto(reto: Reto): Int {
        return withContext(Dispatchers.IO) {
            retoDao.updateReto(reto)
        }
    }

    // Método para eliminar un reto
    suspend fun eliminarReto(retoId: Int): Int = withContext(Dispatchers.IO) {
        retoDao.deleteReto(retoId)
    }
}
