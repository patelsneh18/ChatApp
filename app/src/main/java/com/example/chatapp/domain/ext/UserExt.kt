package com.example.chatapp.domain.ext

import com.example.chatapp.domain.model.User
import com.example.chatapp.helper.userInitialBasedProfileImage

fun User.id(): String {
    return id ?: error("id not found")
}

fun User.profileImageUrl(): String {
    return profileImageUrl ?: userInitialBasedProfileImage(name)
}