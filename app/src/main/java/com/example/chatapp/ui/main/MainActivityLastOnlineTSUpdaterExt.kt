package com.example.chatapp.ui.main

import com.example.chatapp.domain.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun MainActivity.initiateOnlineTSUpdater() {
    execute(false) {
        if (localRepo.isLoggedIn()) {
            onlineTSUpdaterJob = launch {
                updateOnlineTS()
            }
        }
    }
}

private suspend fun MainActivity.updateOnlineTS() {
    lastOnlineTSUpdater.update()
    delay(Constants.OnlineTSUpdater.UPDATE_IN_INTERVAL)
    updateOnlineTS()
}