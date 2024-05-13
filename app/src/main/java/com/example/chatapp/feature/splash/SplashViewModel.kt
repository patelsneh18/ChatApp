package com.example.chatapp.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.ui.Screen
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val localRepo: LocalRepo
): ViewModel() {

    fun checkLoginStatus(
        navController: NavController
    ) {
        viewModelScope.launch {
            if (localRepo.isLoggedIn()) {
                navController.navigate(Screen.Home.route)
            } else navController.navigate(Screen.Login.route)
        }
    }
}