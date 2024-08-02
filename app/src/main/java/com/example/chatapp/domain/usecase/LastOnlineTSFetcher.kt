package com.example.chatapp.domain.usecase

import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.helper.ext.isOnline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LastOnlineTSFetcher(
    private val userRepo: UserRepo
) {

    private lateinit var flow: Flow<Map<String, Boolean>>
    // UserId -> OnlineStatus Map
    private val userOnlineStatus = mapOf<String, Boolean>()

    suspend fun getOnlineStatusOfAllUsers(): Flow<Map<String, Boolean>> {
        initializeFlow()

        return flow
    }

    private suspend fun initializeFlow() {
        if (::flow.isInitialized) {
            flow = userRepo.getAllUsersFlow().map { users ->
                userOnlineStatus.toMutableMap().apply {
                    users.forEach { user ->
                        put(user.id(), user.lastOnlineTS.isOnline())
                    }
                }
            }
        }
    }

    suspend fun getOnlineStatusOf(userId: String): Flow<Boolean> {
        initializeFlow()

        return flow.map { map ->
            map[userId] ?: false
        }
    }
}