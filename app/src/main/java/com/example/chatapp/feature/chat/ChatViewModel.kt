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
import com.streamliners.utils.DateTimeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo
) : BaseViewModel() {

    sealed class ChatListItem {
        class ReceivedMessage(
            val time: String,
            val message: Message
        ) : ChatListItem()

        class Date(val date: String) : ChatListItem()
        class SentMessage(
            val time: String,
            val message: Message
        ) : ChatListItem()
    }

    class Data(
        val channel: Channel,
        val user: User,
        val chatListItems: List<ChatListItem>
    )

    val data = taskStateOf<Data>()
    fun start(
        channelId: String
    ) {
        execute {
            val user = localRepo.getLoggedInUser()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {
                    val channel = channelRepo.getChannel(channelId)
                    data.update(
                        Data(channel, user, createChatListItems(channel, user.id()))
                    )
                }
            }
        }
    }

    fun createChatListItems(channel: Channel, currentUserId: String): List<ChatListItem> {
        val chatItems = mutableListOf<ChatListItem>()
        var prevDateString = ""
        for (message in channel.messages) {
            val dateString = DateTimeUtils.formatTime(
                DateTimeUtils.Format.DATE_MONTH_YEAR_1,
                message.timestamp.toDate().time
            )

            if (prevDateString != dateString) {
                chatItems.add(ChatListItem.Date(dateString))
                prevDateString = dateString
            }

            val chatListItem = if (message.sender == currentUserId) {
                ChatListItem.SentMessage(message.timestamp.toString(), message)
            } else {
                ChatListItem.ReceivedMessage(message.timestamp.toString(), message)
            }
            chatItems.add(chatListItem)
        }
        return chatItems
    }

    fun sendMessage(
        messageStr: String,
        onSuccess: () -> Unit
    ) {
        execute {
            val message = Message(
                sender = data.value().user.id(),
                message = messageStr,
                mediaUrl = null
            )
            channelRepo.sendMessage(data.value().channel.id(), message)
            executeOnMain { onSuccess() }
        }
    }
}