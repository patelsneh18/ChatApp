package com.example.chatapp.domain.ext

import com.example.chatapp.domain.model.Channel

fun Channel.id(): String {
    return id ?: error("channel id not found")
}
