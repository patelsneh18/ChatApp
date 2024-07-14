package com.example.chatapp.feature.fcm

import android.util.Log
import com.example.chatapp.BuildConfig
import com.example.chatapp.MainActivity
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.helper.Base64Util
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.base.exception.defaultExecuteHandlingError
import com.streamliners.helpers.NotificationHelper
import org.koin.android.ext.android.inject

class ChatAppFCMService: FirebaseMessagingService() {

    private val userRepo: UserRepo by inject()
    private val localRepo: LocalRepo by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        defaultExecuteHandlingError(
            lambda = {
                val objectStr = data["object"] ?: error("Notification Object not found")
                when (val notification = Base64Util.decodeJson<Notification>(objectStr)) {
                    is Notification.NewMessageNotification -> handleNewMessageNotification(notification)
                }
            }, buildType = BuildConfig.BUILD_TYPE
        )
    }

    private suspend fun handleNewMessageNotification(notification: Notification.NewMessageNotification) {
        val localUserId = localRepo.getLoggedInUser().id()

        if (notification.senderUserId == localUserId) {
            Log.i("ChatAppDebug", "Skipping notification as it is self sent")
            return
        }

        notification.run {
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, body: String) {
        NotificationHelper(this)
            .showNotification(
                title = title,
                body = body,
                pendingIntentActivity = MainActivity::class.java
            )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        defaultExecuteHandlingError(
            lambda = {
                if (localRepo.isLoggedIn()) {
                    val user = localRepo.getLoggedInUser().copy(
                        fcmToken = token
                    )
                    localRepo.upsertCurrentUser(user)
                    userRepo.updateFcmToken(token, user.id())
                }
            },
            buildType = BuildConfig.BUILD_TYPE
        )
    }

}