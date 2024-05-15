package com.example.chatapp.domain.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String? = null,
    var name: String,
    var email: String,
    val profileImageUrl: String?,
    var bio: String,
    var gender: Gender
) {
    constructor() : this(id = null, name = "", email = "", profileImageUrl = "", bio = "", gender = Gender.Male)
}
