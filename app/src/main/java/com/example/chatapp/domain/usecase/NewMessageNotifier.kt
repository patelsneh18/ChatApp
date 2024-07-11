package com.example.chatapp.domain.usecase

import com.example.chatapp.data.remote.OtherRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.helper.FcmMessage
import com.example.chatapp.helper.FcmPayload
import com.example.chatapp.helper.FcmSender
import com.example.chatapp.helper.NotificationPayload

class NewMessageNotifier (
    private val userRepo: UserRepo,
    private val otherRepo: OtherRepo,
    private val fcmSender: FcmSender,
){
    suspend fun notifySingleUser(
        senderName: String,
        userId: String,
        message: String
    ) {
        val token = userRepo.getUserWithId(userId).fcmToken
        val payload = FcmPayload (
            FcmMessage.forToken(
                token = token,
                notification = NotificationPayload(
                    title = "New Message",
                    body = "$senderName: $message"
                )
            )
        )
        sendNotification(payload)
    }

    suspend fun notifyMultipleUsersUsingTopic(
        senderName: String,
        topic: String,
        message: String
    ) {
        val payload = FcmPayload (
            FcmMessage.forTopic(
                topic = topic,
                notification = NotificationPayload(
                    title = "New Message",
                    body = "$senderName: $message"
                )
            )
        )
        sendNotification(payload)
    }

    private suspend fun sendNotification(payload: FcmPayload) {
        val svcAccJson = otherRepo.getServiceAccountJson()
        fcmSender.send(payload, svcAccJson)
    }
}