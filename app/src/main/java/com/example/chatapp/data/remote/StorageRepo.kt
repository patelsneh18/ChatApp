package com.example.chatapp.data.remote

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class StorageRepo {

    suspend fun saveFile(path: String, uri: Uri): String {
        Firebase.storage.getReference(path).putFile(uri).await()

        val downloadUrl = Firebase.storage.getReference(path).downloadUrl.await()
            ?: throw IllegalStateException("Unable to get URL")

        return downloadUrl.toString()
    }
}