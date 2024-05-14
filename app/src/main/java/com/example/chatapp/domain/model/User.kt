package com.example.chatapp.domain.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String? = null,
    var name: String,
    var email: String,
    var bio: String,
    var gender: Gender
) {
    constructor() : this(id = null, name = "", email = "", bio = "", gender = Gender.Male)
}
