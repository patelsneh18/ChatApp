package com.example.chatapp.domain

import kotlin.time.Duration.Companion.minutes

object Constants {
    object OnlineTSUpdater {
        val UPDATE_IN_INTERVAL = 1L.minutes.inWholeMilliseconds
    }
}