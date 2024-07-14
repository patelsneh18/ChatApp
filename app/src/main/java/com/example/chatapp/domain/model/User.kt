package com.example.chatapp.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String? = null,
    var name: String,
    var email: String,
    val profileImageUrl: String?,
    var bio: String,
    var gender: Gender,
    var dob: String?,
    val fcmToken: String? = null,
    val lastOnlineTS: Timestamp
) {
    constructor() : this(null, "", "",  "", "",Gender.Male, dob = null, null, Timestamp.now())
}
