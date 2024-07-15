package com.example.chatapp.feature.home

import androidx.compose.runtime.mutableStateMapOf
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.Constants
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.ext.otherUserId
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.tasks.await
import java.lang.System.currentTimeMillis

class HomeViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo,
    private val userRepo: UserRepo
): BaseViewModel() {

    data class State(
        val channels: List<Channel>,
        val user: User
    )
    val state = taskStateOf<State>()
    private val userOnlineStatus = mutableStateMapOf<String, Boolean>()

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

            checkUserOnlineStatus(users)

            subscribeForGroupNotifications()
        }
    }

    private fun checkUserOnlineStatus(users: List<User>) {
        users.forEach { user ->
            userOnlineStatus[user.id()] = user.lastOnlineTS.isOnline()
        }
    }

    fun isChannelOneToOneAndOnline(channel: Channel): Boolean {
        return if (channel.type == Channel.Type.OneToOne) {
            val otherUserId = channel.otherUserId(state.value().user.id())
            userOnlineStatus[otherUserId] ?: false
        } else false
    }
    private fun Timestamp?.isOnline(): Boolean {
        return this?.let {
            toDate().time + Constants.OnlineTSUpdater.EXPIRE_STATUS_INTERVAL >= currentTimeMillis()
        } ?: false
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