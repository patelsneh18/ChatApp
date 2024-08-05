package com.example.chatapp.domain.ext

import com.example.chatapp.domain.model.Channel
import com.example.chatapp.domain.model.User
import com.example.chatapp.helper.userInitialBasedProfileImage

fun Channel.id(): String {
    return id ?: error("channel id not found")
}

fun Channel.otherUserId(currentUserId: String): String {
    return members.find { it != currentUserId }
            ?: error("otherUserIdNotFound")
}

fun Channel.imageUrl(): String {
    return imageUrl ?: userInitialBasedProfileImage(name)
}