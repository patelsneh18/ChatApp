package com.example.chatapp.helper

fun userInitialBasedProfileImage(name: String): String {
    return "https://placehold.co/400/B679C6/FFFFFF/png?text=${name.first()}"
}