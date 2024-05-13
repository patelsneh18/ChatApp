package com.example.chatapp.data

import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User) {
        Firebase.firestore
            .collection("users")
            .add(user)
            .await()
    }
}