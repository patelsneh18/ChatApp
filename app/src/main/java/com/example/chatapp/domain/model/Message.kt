package com.example.chatapp.domain.model

import com.google.firebase.Timestamp

data class Message(
    val timestamp: Timestamp,
    // user id of sender
    val sender: String,
    val message: String,
    val mediaUrl: String?
)
