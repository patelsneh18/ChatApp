package com.example.chatapp.feature.login

import androidx.navigation.NavController
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.ext.id
import com.example.chatapp.helper.ext.navigateTo
import com.example.chatapp.ui.Screen
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
): BaseViewModel() {

    fun onLoggedIn(email: String, navController: NavController) {
        execute {
            var user = userRepo.getUserWithEmail(email)

            // Subscribe to FCM Topic
            Firebase.messaging.subscribeToTopic("general").await()
            if (user != null) {

                val token = Firebase.messaging.token.await()
                user = user.copy(fcmToken = token)

                // update fcm token
                userRepo.updateFcmToken(token, user.id())

                // save in local repo and navigate
                localRepo.upsertCurrentUser(user)
                executeOnMain { navController.navigate(Screen.Home.route) }
            } else {
                //navigate to edit profile
                executeOnMain { navController.navigateTo(Screen.EditProfile(email), Screen.Login) }
            }
        }
    }
}