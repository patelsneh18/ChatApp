package com.example.chatapp.feature.newGroupChat

import androidx.core.net.toUri
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.exception.failure
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.pickers.media.PickedMedia

class NewGroupChatViewModel(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val channelRepo: ChannelRepo,
    private val storageRepo: StorageRepo
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

    fun createGroupChannel(
        name: String,
        description: String?,
        groupImage: PickedMedia?,
        members: List<String>,
        onChannelReady: (String) -> Unit
    ) {
        execute {
            if(members.size < 2) failure("Members must be >= 2")
            val currentUserId = localRepo.getLoggedInUser().id()

            val groupImageUrl = uploadGroupImage(name, groupImage)

            val channelId = channelRepo.createGroupChannel(
                currentUserId, name, description, groupImageUrl, members
            )

            executeOnMain { onChannelReady(channelId) }
        }
    }

    private suspend fun uploadGroupImage(name: String, image: PickedMedia?): String? {
        return image?.let {
            // TODO Save Image using user id
            // TODO Use the exact file extension
            storageRepo.saveFile("groupImages/$name-${System.currentTimeMillis()}.jpg", it.uri.toUri())
        }
    }
}