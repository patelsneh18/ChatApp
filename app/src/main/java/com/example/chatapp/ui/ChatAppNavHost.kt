package com.example.chatapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.editProfile.EditProfileScreen
import com.example.chatapp.feature.login.LoginScreen
import com.example.chatapp.feature.splash.SplashScreen

@Composable
fun ChatAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.name) {
        composable(Screen.Splash.name) {
            SplashScreen()
        }

        composable(Screen.Login.name) {
            LoginScreen(navController)
        }

        composable(Screen.EditProfile.name) {
            EditProfileScreen()
        }
    }
}