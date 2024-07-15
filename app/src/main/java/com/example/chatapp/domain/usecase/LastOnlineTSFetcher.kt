package com.example.chatapp.domain.usecase

import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.helper.ext.isOnline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LastOnlineTSFetcher(
    private val userRepo: UserRepo
) {
    // UserId -> OnlineStatus Map
    private val userOnlineStatus = mapOf<String, Boolean>()

    suspend fun getOnlineStatusOfAllUsers(): Flow<Map<String, Boolean>> {
        return userRepo.getAllUsersFlow().map { users ->
            userOnlineStatus.toMutableMap().apply {
                users.forEach { user ->
                    put(user.id(), user.lastOnlineTS.isOnline())
                }
            }
        }
    }
}