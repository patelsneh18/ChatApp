package com.example.chatapp.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.ui.Screen
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
): ViewModel() {

    fun onLoggedIn(email: String, navController: NavController) {
        viewModelScope.launch {
            val user = userRepo.getUserWithEmail(email)

            if (user != null) {
                localRepo.onLoggedIn(user)
                navController.navigate(Screen.Home.route)
            } else {
                //navigate to edit profile
                navController.navigate(Screen.EditProfile(email).route)
            }
        }
    }
}