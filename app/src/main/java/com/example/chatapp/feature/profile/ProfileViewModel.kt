package com.example.chatapp.feature.profile

import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class ProfileViewModel(
    private val userRepo: UserRepo
): BaseViewModel() {

    val userTask = taskStateOf<User>()
    var user: User? = null
    fun start(userId: String) {
        execute {
            userTask.load {
                user = userRepo.getUserWithId(userId)
                user ?: error("User not found with id: $userId")
            }
        }
    }
}