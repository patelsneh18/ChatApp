package com.example.chatapp.feature.chat

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value

class ChatViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo
): BaseViewModel() {

    val channel = taskStateOf<Channel>()
    lateinit var user: User
    fun start(
        channelId: String
    ) {
        execute {
            user = localRepo.getLoggedInUser()
            channel.update(channelRepo.getChannel(channelId))
        }
    }

    fun sendMessage(messageStr: String) {
        execute {
            val message = Message (
                sender = user.id(),
                message = messageStr,
                mediaUrl = null
            )
            channelRepo.sendMessage(channel.value().id(), message)
        }
    }
}