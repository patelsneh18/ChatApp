package com.example.chatapp.feature.newChat

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class NewChatViewModel(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val channelRepo: ChannelRepo
) : BaseViewModel() {

    val userListTask = taskStateOf<List<User>>()

    fun start() {

        execute {
            val user = localRepo.getLoggedInUser()
            userListTask.load {
                val id = user.id
                userRepo.getAllUsers().filter {
                    it.id != id
                }
            }
        }
    }

    fun onUserSelected(
        otherUserId: String,
        onChannelReady: (String) -> Unit
    ) {
        execute {
            val currentUserId = localRepo.getLoggedInUser().id()
            val channel = channelRepo.getOneToOneChannel(currentUserId, otherUserId)

            val channelId = if (channel != null) {
                channel.id()
            } else {
                channelRepo.createOneToOneChannel(currentUserId, otherUserId)
            }

            executeOnMain { onChannelReady(channelId) }
        }
    }

}