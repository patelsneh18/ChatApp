package com.example.chatapp.domain.usecase

import com.example.chatapp.data.remote.OtherRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.feature.fcm.Notification
import com.example.chatapp.helper.Base64Util
import com.example.chatapp.helper.FcmMessage
import com.example.chatapp.helper.FcmPayload
import com.example.chatapp.helper.FcmSender

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
        val newMessageNotification = Notification.NewMessageNotification(
            title = "New Message",
            body = "$senderName : $message"
        )
        val payload = FcmPayload (
            FcmMessage.forToken(
                token = token,
                data = mapOf(
                    "object" to Base64Util.encodeAsJson(newMessageNotification, Notification.supportingGson())
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
        val newMessageNotification = Notification.NewMessageNotification(
            title = "New Message",
            body = "$senderName : $message"
        )
        val payload = FcmPayload (
            FcmMessage.forTopic(
                topic = topic,
                data = mapOf(
                    "object" to Base64Util.encodeAsJson(newMessageNotification, Notification.supportingGson())
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