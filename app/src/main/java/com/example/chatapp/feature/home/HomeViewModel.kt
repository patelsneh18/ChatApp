package com.example.chatapp.feature.home

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.domain.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.tasks.await

class HomeViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo,
    private val userRepo: UserRepo
): BaseViewModel() {

    val channelsState = taskStateOf<List<Channel>>()

    fun start() {
        execute {
            val userId = localRepo.getLoggedInUser().id()

            val users = userRepo.getAllUsers()
            val channels = channelRepo.getAllChannelsOf(userId)
                .map { channel ->
                    if (channel.type == Channel.Type.OneToOne) {
                        val otherUserId = channel.members.find { it != userId }
                            ?: error("otherUserIdNotFound")

                        val otherUser = users.find { it.id() == otherUserId }
                            ?: error("user with Id not found")

                        channel.copy(
                            name = otherUser.name,
                            imageUrl = otherUser.profileImageUrl()
                        )
                    } else {
                        channel
                    }
                }
            channelsState.update(channels)

            subscribeForGroupNotifications()
        }
    }

    private fun subscribeForGroupNotifications() {
        execute(false) {
            channelsState.value()
                .filter { it.type == Channel.Type.Group }
                .forEach { channel ->
                    Firebase.messaging.subscribeToTopic(channel.id())
                        .await()
                }
        }
    }
}