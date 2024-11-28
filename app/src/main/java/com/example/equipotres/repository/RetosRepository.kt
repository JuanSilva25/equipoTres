package com.example.equipotres.repository

import android.util.Log
import com.example.equipotres.model.Reto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetosRepository @Inject constructor(
    private val db: FirebaseFirestore  // Inyección de FirebaseFirestore
) {

    private val retosCollection = db.collection("retos")

    // Agregar un reto
    suspend fun agregarReto(reto: Reto) {
        val document = retosCollection.document()
        reto.id = document.id
        document.set(reto).await()
    }

    // Obtener lista de retos para un usuario
    fun obtenerListaRetos(userId: String): Flow<List<Reto>> = flow {
        try {
            Log.d("RetosRepository", "Consultando retos para userId: $userId")
            val snapshot = retosCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            val retos = snapshot.toObjects(Reto::class.java)
            Log.d("RetosRepository", "Retos obtenidos: $retos")
            emit(retos)
        } catch (e: Exception) {
            Log.e("RetosRepository", "Error al obtener retos: ${e.message}")
            emit(emptyList())
        }
    }

    // Obtener un reto aleatorio
    suspend fun obtenerRetoAleatorio(userId: String): Reto? {
        val snapshot = retosCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
        val retos = snapshot.toObjects(Reto::class.java)
        return retos.randomOrNull()
    }

    // Actualizar un reto completo
    suspend fun actualizarReto(reto: Reto) {
        retosCollection.document(reto.id).set(reto).await()
    }

    // Eliminar un reto
    suspend fun eliminarReto(retoId: String) {
        retosCollection.document(retoId).delete().await()
    }
}