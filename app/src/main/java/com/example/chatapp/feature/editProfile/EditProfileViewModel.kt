package com.example.chatapp.feature.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.UserRepo
import com.example.chatapp.domain.model.User
import kotlinx.coroutines.launch

class EditProfileViewModel: ViewModel() {

    fun saveUser(user: User, onSuccess: () -> Unit){
        viewModelScope.launch {
            UserRepo().saveUser(user)
            onSuccess()
        }
    }
}