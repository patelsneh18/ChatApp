package com.example.chatapp.feature.groupInfo

import androidx.compose.runtime.mutableStateOf
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.User
import com.example.chatapp.domain.usecase.LastOnlineTSFetcher
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class  GroupInfoViewModel(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo,
    private val lastOnlineTSFetcher: LastOnlineTSFetcher
): BaseViewModel() {

    val channelTask = taskStateOf<Channel>()
    var channel: Channel? = null
    val userOnlineStatus = mutableStateOf<Map<String, Boolean>>(emptyMap())

    var users = emptyList<User>()
    fun start(channelId: String) {
        execute {
            launch {
                checkUserOnlineStatus()
            }

            channelTask.load {
                channel = channelRepo.getChannel(channelId)
                users = userRepo.getUsers(channel?.members ?: emptyList())
                channel ?: error("Channel not found with id: $channelId")
            }
        }
    }

    private suspend fun checkUserOnlineStatus() {
        lastOnlineTSFetcher.getOnlineStatusOfAllUsers().collectLatest { map ->
            userOnlineStatus.value = map
        }
    }
}