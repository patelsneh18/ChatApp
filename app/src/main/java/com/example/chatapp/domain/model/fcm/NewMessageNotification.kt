package com.example.chatapp.domain.model.fcm

data class NewMessageNotification(
    val title: String,
    val body: String,
    val senderUserId: String? = null
)