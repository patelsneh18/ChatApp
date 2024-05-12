package com.example.chatapp.ui

sealed class Screen(
    val route: String
) {
    data object Splash : Screen("Splash")
    data object Login : Screen("Login")
    class EditProfile(
        val email: String
    ) : Screen("EditProfile?email=$email") {

        companion object {
            fun format() = "EditProfile?email={email}"
        }
    }
}