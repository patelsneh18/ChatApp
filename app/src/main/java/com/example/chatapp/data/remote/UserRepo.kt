package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.FirestoreCollections.usersColl
import com.example.chatapp.domain.ext.id
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

    suspend fun upsertUser(user: User) {
        Firebase.firestore
            .usersColl()
            .document(user.id())
            .set(user)
            .await()
    }
    suspend fun getAllUsers(): List<User>{
        return Firebase.firestore
            .usersColl()
            .get()
            .await()
            .toObjects(User::class.java)
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

    suspend fun getUserWithId(userId: String) : User {
        return Firebase.firestore
            .usersColl()
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
            ?: error("No user found with id: $userId")
    }

    suspend fun updateFcmToken(token: String, userId: String) {
        Firebase.firestore
            .usersColl()
            .document(userId)
            .update(User::fcmToken.name, token)
            .await()
    }
}