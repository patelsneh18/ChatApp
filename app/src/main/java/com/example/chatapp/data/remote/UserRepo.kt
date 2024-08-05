package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.FirestoreCollections.usersColl
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User) {
        Firebase.firestore
            .usersColl()
            .add(user)
            .await()
    }

    suspend fun upsertUser(user: User): User{
        val collRef = Firebase.firestore.usersColl()
        val id = user.id ?: collRef.document().id
        collRef.document(id).set(user).await()
        return user.copy(id = id)
    }

    suspend fun getAllUsers(): List<User>{
        return Firebase.firestore
            .usersColl()
            .get()
            .await()
            .toObjects(User::class.java)
    }

    suspend fun getAllUsersFlow(): Flow<List<User>> {
        return callbackFlow {
            val registration = Firebase.firestore
                .usersColl()
                .addSnapshotListener { value, error ->
                    //handle error
                    error?.let {
                        it.printStackTrace()
                        error(it.message ?: "FirestoreException")
                    }

                    //Parse docs as List<User>
                    val users = value?.toObjects(User::class.java) ?: error("Users not found")

                    //Emit the list
                    CoroutineScope(coroutineContext).launch {
                        send(users)
                    }
                }

            //Remove snapshot listener when coroutine cancels
            awaitClose {
                registration.remove()
            }
        }
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

    suspend fun updateOnlineTimestamp(userId: String) {
        Firebase.firestore
            .usersColl()
            .document(userId)
            .update(User::lastOnlineTS.name, Timestamp.now())
            .await()
    }

    suspend fun getUsers(userIds: List<String>): List<User> {
        val users = mutableListOf<User>()

        userIds.forEach {
            users.add(
                Firebase.firestore
                    .usersColl()
                    .document(it)
                    .get()
                    .await()
                    .toObject(User::class.java) ?: error("User not found with $it")
            )
        }

        return users
    }
}