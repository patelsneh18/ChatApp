package com.example.chatapp.feature.chat

import androidx.core.net.toUri
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.StorageRepo
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
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
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
        return buildList {
        var prevDateString = ""

            channel.messages.forEach { message->
                val dateString = DateTimeUtils.formatTime(
                    DateTimeUtils.Format.DATE_MONTH_YEAR_1,
                    message.timestamp.toDate().time
                )

                if (prevDateString != dateString) {
                    add(ChatListItem.Date(dateString))
                    prevDateString = dateString
                }

                val chatListItem = if (message.sender == currentUserId) {
                    ChatListItem.SentMessage(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12,
                            message.timestamp.toDate().time
                        ), message)
                } else {
                    ChatListItem.ReceivedMessage(
                        DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.timestamp.toDate().time
                        ), message)
                }

                add(chatListItem)
            }
        }
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

    fun sendImage(uri: String) {
        execute {
            val email = localRepo.getLoggedInUser().email
            val timestamp = System.currentTimeMillis()
            val imageUrl = storageRepo.saveFile("media/$timestamp-$email.jpg", uri.toUri())

            val message = Message(
                sender = data.value().user.id(),
                message = "",
                mediaUrl = imageUrl
            )

            channelRepo.sendMessage(data.value().channel.id(), message)
        }
    }
}