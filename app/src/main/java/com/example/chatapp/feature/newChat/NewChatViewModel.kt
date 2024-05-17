package com.example.chatapp.feature.newChat

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf

class NewChatViewModel(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
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
}