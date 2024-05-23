package com.example.chatapp.feature.chat

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo
): BaseViewModel() {

    class Data(
        val channel: Channel,
        val user: User
    )
    val data = taskStateOf<Data>()
    fun start(
        channelId: String
    ) {
        execute {
            val user = localRepo.getLoggedInUser()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {
                    data.update(
                        Data(channelRepo.getChannel(channelId), user)
                    )
                }
            }
        }
    }

    fun sendMessage(
        messageStr: String,
        onSuccess: () -> Unit
    ) {
        execute {
            val message = Message (
                sender = data.value().user.id(),
                message = messageStr,
                mediaUrl = null
            )
            channelRepo.sendMessage(data.value().channel.id(), message)
            executeOnMain { onSuccess() }
        }
    }
}