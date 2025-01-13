package com.example.firebase.repository

import android.util.Log
import com.example.firebase.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkRepository (
    private val firestore: FirebaseFirestore
): RepositoryMhs {
    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (
            e: Exception
        ) {
            throw Exception("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            val querySnapshot = firestore.collection("Mahasiswa")
                .whereEqualTo("nim", mahasiswa.nim)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()
        }catch (e: Exception) {
            throw Exception("Gagal mengupdate data mahasiswa: ${e.message}")
        }
    }

    override fun getAllMhs(): Flow<List<Mahasiswa>> = callbackFlow {
       val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.DESCENDING)
            .addSnapshotListener{ value, error ->
                if (value != null) {
                    val mhsList = value.documents.mapNotNull {
                        //Convert dari document firestore ke dataclass
                        it.toObject(Mahasiswa::class.java)
                    }
                    //Fungsi untuk mengirim collection ke data class
                    trySend(mhsList)
                }
            }
        //menutup collection dari firestore
        awaitClose{
            mhsCollection.remove()
        }
    }

    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsCollection = firestore.collection("Mahasiswa")
            .whereEqualTo("nim", nim) // Query based on the "nim" field

        mhsCollection.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Log error and close the flow
                Log.e("Firestore", "Error fetching documents: $error")
                close(error)
                return@addSnapshotListener
            }

            querySnapshot?.let {
                // If documents are found
                if (!it.isEmpty) {
                    for (document in it.documents) {
                        // Log raw data for debugging
                        Log.d("Firestore", "Document Data: ${document.data}")

                        // Attempt to map the document to the Mahasiswa object
                        val mhs = document.toObject(Mahasiswa::class.java)
                        if (mhs != null) {
                            // Successfully mapped Mahasiswa object
                            trySend(mhs)
                        } else {
                            // Log mapping failure and close the flow
                            Log.e("Firestore", "Failed to map document to Mahasiswa")
                            close(Exception("Document data is invalid or null"))
                        }
                    }
                } else {
                    // Handle case where no document is found
                    Log.e("Firestore", "No documents found for nim: $nim")
                    close(Exception("No documents found for nim: $nim"))
                }
            } ?: run {
                // Handle case where snapshot is null
                Log.e("Firestore", "No documents found for nim: $nim")
                close(Exception("No documents found for nim: $nim"))
            }
        }

        // Remove the listener when the flow is closed
        awaitClose {
            // Firestore will automatically handle cleanup
        }
    }
}