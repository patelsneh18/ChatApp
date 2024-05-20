package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.FirestoreCollections.channelsColl
import com.example.chatapp.domain.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class ChannelRepo {

    suspend fun getOneToOneChannel(
        currentUserId: String,
        otherUserId: String,
    ): Channel? {
        return Firebase.firestore
            .channelsColl()
            .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
            .whereArrayContainsAny(Channel::members.name, listOf(currentUserId, otherUserId))
            .get()
            .await()
            .toObjects(Channel::class.java)
            .firstOrNull {
                it.members == listOf(currentUserId, otherUserId) ||
                        it.members == listOf(otherUserId, currentUserId)
            }
    }

    suspend fun createOneToOneChannel(
        currentUserId: String,
        otherUserId: String
    ): String {
        val collRef = Firebase.firestore.channelsColl()
        val id = collRef.document().id
        collRef
            .document(id)
            .set(
                Channel (
                    imageUrl = null,
                    type = Channel.Type.OneToOne,
                    name = "OneToOne",
                    description = null,
                    members = listOf(
                        currentUserId, otherUserId
                    ),
                    messages = emptyList()
                )
            )
            .await()

        return id
    }

    suspend fun getAllChannelsOf(userId: String): List<Channel> {
        return Firebase.firestore
            .channelsColl()
            .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
            .whereArrayContains(Channel::members.name, userId)
            .get()
            .await()
            .toObjects(Channel::class.java)
    }
}