package com.example.chatapp.helper.fcm

class FcmPayload(
    val message: FcmMessage
)
class FcmMessage private constructor(
    val topic: String?,
    val token: String?,
    val notification: NotificationPayload?,
    val data: Map<String, Any>?
) {
    companion object {
        fun forTopic(
            topic: String?,
            notification: NotificationPayload? = null,
            data: Map<String, Any>? = null
        ): FcmMessage {
            return FcmMessage(
                topic = topic,
                token = null,
                notification = notification,
                data = data
            )
        }

        fun forToken(
            token: String?,
            notification: NotificationPayload? = null,
            data: Map<String, Any>? = null
        ): FcmMessage {
            return FcmMessage(
                topic = null,
                token = token,
                notification = notification,
                data = data
            )
        }
    }
}

data class NotificationPayload(
    val title: String,
    val body: String
)