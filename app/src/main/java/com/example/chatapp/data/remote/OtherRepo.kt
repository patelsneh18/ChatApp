package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.FirestoreCollections.otherColl
import com.example.chatapp.domain.model.Secret
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class OtherRepo {

    suspend fun getServiceAccountJson(): String {
        return Firebase.firestore
            .otherColl()
            .document("secret")
            .get()
            .await()
            .toObject(Secret::class.java)?.svcAc
            ?: error("svcAc not found on firestore")
    }
}