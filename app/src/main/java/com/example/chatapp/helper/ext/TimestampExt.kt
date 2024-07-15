package com.example.chatapp.helper.ext

import com.example.chatapp.domain.Constants
import com.google.firebase.Timestamp
import java.lang.System.currentTimeMillis

fun Timestamp?.isOnline(): Boolean {
    return this?.let {
        toDate().time + Constants.OnlineTSUpdater.EXPIRE_STATUS_INTERVAL >= currentTimeMillis()
    } ?: false
}