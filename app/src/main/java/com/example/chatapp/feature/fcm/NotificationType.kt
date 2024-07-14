package com.example.chatapp.feature.fcm

import com.example.chatapp.helper.SealedTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder

sealed class Notification {

    data class NewMessageNotification(
        val title: String,
        val body: String,
        val senderUserId: String? = null
    ): Notification()

    companion object {
        fun supportingGson(): Gson = GsonBuilder()
            .registerTypeAdapterFactory(SealedTypeAdapterFactory.of(Notification::class))
            .create()
    }
}