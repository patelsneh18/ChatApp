package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.FirestoreCollections.usersColl
import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User) {
        Firebase.firestore
            .usersColl()
            .add(user)
            .await()
    }

    suspend fun getUserWithEmail(email: String) : User? {
        return Firebase.firestore
            .usersColl()
            .whereEqualTo(User::email.name, email)
            .get()
            .await()
            .toObjects(User::class.java)
            .firstOrNull()
    }
}