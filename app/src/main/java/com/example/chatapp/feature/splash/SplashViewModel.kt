package com.example.chatapp.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.helper.ext.navigateTo
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
                navController.navigateTo(Screen.Home, Screen.Splash)
            } else navController.navigateTo(Screen.Login, Screen.Splash)
        }
    }
}