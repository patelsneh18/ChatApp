package com.example.chatapp.feature.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.UserRepo
import com.example.chatapp.domain.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
): ViewModel() {

    fun saveUser(user: User, onSuccess: () -> Unit){
        viewModelScope.launch {
            userRepo.saveUser(user)
            localRepo.onLoggedIn()
            onSuccess()
        }
    }
}