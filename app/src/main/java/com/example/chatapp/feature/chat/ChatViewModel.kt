package com.example.chatapp.feature.chat

import androidx.core.net.toUri
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.model.User
import com.example.chatapp.domain.usecase.NewMessageNotifier
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
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo,
    private val newMessageNotifier: NewMessageNotifier
) : BaseViewModel() {

    sealed class ChatListItem {
        class ReceivedMessage(
            val time: String,
            val senderName: String?,
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
            //TODO: Fetch list only if Group Channel
            val users = userRepo.getAllUsers()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {
                    val channel = channelRepo.getChannel(channelId)
                    data.update(
                        Data(channel, user, createChatListItems(channel, user.id(), users))
                    )
                }
            }
        }
    }

    private fun createChatListItems(channel: Channel, currentUserId: String, users: List<User>): List<ChatListItem> {
        return buildList {
            var prevDateString = ""

            channel.messages.forEach { message ->
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
                        ), message
                    )
                } else {
                    val name = if (channel.type == Channel.Type.Group) {
                        users.find { it.id() == message.sender }?.name
                            ?: error("User with id: ${message.sender} not found")
                    } else null

                    ChatListItem.ReceivedMessage(
                        time = DateTimeUtils.formatTime(
                            DateTimeUtils.Format.HOUR_MIN_12, message.timestamp.toDate().time
                        ),
                        senderName = name,
                        message = message
                    )
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
            notifyOtherUser(messageStr)
            executeOnMain { onSuccess() }
        }
    }

    private fun notifyOtherUser(message: String) {
        val channel = data.value().channel
        val user = data.value().user
        when (channel.type) {
            Channel.Type.OneToOne -> notifySingleUserUsingToken(channel, user, message)
            Channel.Type.Group -> notifyAllOtherUserUsingTopic(channel, message)
        }
    }

    private fun notifyAllOtherUserUsingTopic(channel: Channel, message: String) {
        // TODO("Send to all users except current user")
        execute(showLoadingDialog = false) {
            newMessageNotifier.notifyMultipleUsersUsingTopic(
                data.value().user.name,
                topic = channel.id(),
                message = message
            )
        }
    }

    private fun notifySingleUserUsingToken(
        channel: Channel,
        user: User,
        message: String
    ) {
        val otherUserId = channel.members.find { it != user.id() }
            ?: error("otherUserIdNotFound")
        execute(showLoadingDialog = false) {
            newMessageNotifier.notifySingleUser(
                data.value().user.name,
                userId = otherUserId,
                message = message
            )
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
            notifyOtherUser("Sent an Image")
            channelRepo.sendMessage(data.value().channel.id(), message)
        }
    }
}