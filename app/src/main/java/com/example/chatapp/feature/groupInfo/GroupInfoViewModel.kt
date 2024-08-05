package com.example.chatapp.feature.groupInfo

import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class  GroupInfoViewModel(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo
): BaseViewModel() {

    val channelTask = taskStateOf<Channel>()
    var channel: Channel? = null
    var users = emptyList<User>()
    fun start(channelId: String) {
        execute {
            channelTask.load {
                channel = channelRepo.getChannel(channelId)
                users = userRepo.getUsers(channel?.members ?: emptyList())
                channel ?: error("Channel not found with id: $channelId")
            }
        }
    }
}