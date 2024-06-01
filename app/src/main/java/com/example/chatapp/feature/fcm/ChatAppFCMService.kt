package com.example.chatapp.feature.fcm

import com.example.chatapp.BuildConfig
import com.example.chatapp.MainActivity
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.base.exception.defaultExceptionHandler
import com.streamliners.base.exception.defaultExecuteHandlingError
import com.streamliners.helpers.NotificationHelper
import org.koin.android.ext.android.inject
import javax.inject.Inject

class ChatAppFCMService: FirebaseMessagingService() {

    private val userRepo: UserRepo by inject()
    private val localRepo: LocalRepo by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationParams = message.notification ?: return
        val title = notificationParams.title ?: return
        val body = notificationParams.body ?: return
        showNotification(title, body)
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