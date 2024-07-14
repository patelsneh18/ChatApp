package com.example.chatapp.domain.usecase

import android.util.Log
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id

class LastOnlineTSUpdater(
    private val localRepo: LocalRepo,
    private val userRepo: UserRepo
) {

    suspend fun update() {
        val userId = localRepo.getLoggedInUser().id()
        userRepo.updateOnlineTimestamp(userId)
        Log.i("ChatAppDebug", "updating online TS")
    }
}