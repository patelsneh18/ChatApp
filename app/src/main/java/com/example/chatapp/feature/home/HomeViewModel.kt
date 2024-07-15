package com.example.chatapp.feature.home

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.Constants
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.ext.otherUserId
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.User
import com.example.chatapp.domain.usecase.LastOnlineTSFetcher
import com.example.chatapp.helper.ext.isOnline
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.System.currentTimeMillis

class HomeViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo,
    private val userRepo: UserRepo,
    private val lastOnlineTSFetcher: LastOnlineTSFetcher
): BaseViewModel() {

    data class State(
        val channels: List<Channel>,
        val user: User
    )
    val state = taskStateOf<State>()
    val userOnlineStatus = mutableStateOf<Map<String, Boolean>>(emptyMap())

    fun start() {
        execute {
            val user = localRepo.getLoggedInUser()
            val userId = localRepo.getLoggedInUser().id()

            val users = userRepo.getAllUsers()
            val channels = channelRepo.getAllChannelsOf(userId)
                .map { channel ->
                    if (channel.type == Channel.Type.OneToOne) {
                        val otherUserId = channel.otherUserId(userId)
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
            state.update(State(channels, user))

            launch {
                checkUserOnlineStatus(users)
            }

            subscribeForGroupNotifications()
        }
    }

    private suspend fun checkUserOnlineStatus(users: List<User>) {
        lastOnlineTSFetcher.getOnlineStatusOfAllUsers().collectLatest { map ->
            userOnlineStatus.value = map
        }
    }

    fun isChannelOneToOneAndOnline(channel: Channel): Boolean {
        return if (channel.type == Channel.Type.OneToOne) {
            val otherUserId = channel.otherUserId(state.value().user.id())
            userOnlineStatus.value[otherUserId] ?: false
        } else false
    }

    private fun subscribeForGroupNotifications() {
        execute(false) {
            state.value()
                .channels
                .filter { it.type == Channel.Type.Group }
                .forEach { channel ->
                    Firebase.messaging.subscribeToTopic(channel.id())
                        .await()
                }
        }
    }
}