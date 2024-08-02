package com.example.chatapp.ui

sealed class Screen(
    val route: String
) {
    data object Splash : Screen("Splash")
    data object Login : Screen("Login")
    data object Home : Screen("Home")
    data object NewChat : Screen("NewChat")
    data object NewGroupChat : Screen("NewGroupChat")
    class EditProfile(
        val email: String
    ) : Screen("EditProfile?email=$email") {

        companion object {
            fun format() = "EditProfile?email={email}"
        }
    }

    class Chat(
        channelId: String
    ) : Screen("Chat?channelId=$channelId") {

        companion object {
            fun format() = "Chat?channelId={channelId}"
        }
    }

    class Profile(
        val userId: String
    ) : Screen("Profile?userId=$userId") {

        companion object {
            fun format() = "Profile?userId={userId}"
        }
    }
}