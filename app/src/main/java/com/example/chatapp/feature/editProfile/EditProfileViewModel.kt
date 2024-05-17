package com.example.chatapp.feature.editProfile

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.User
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.pickers.media.PickedMedia
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
): BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()
    fun saveUser(user: User, image: PickedMedia?, onSuccess: () -> Unit){

        execute(showLoadingDialog = false){
            saveProfileTask.load {
                val updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(user.email, image)
                )
                userRepo.saveUser(updatedUser)
                localRepo.onLoggedIn(updatedUser)
                executeOnMain { onSuccess() }
            }
        }
    }

    private suspend fun uploadProfileImage(email: String, image: PickedMedia?): String? {
        return image?.let {
            // TODO Save Image using user id
            // TODO Use the exact file extension
            storageRepo.saveFile("profileImages/$email.jpg", it.uri.toUri())
        }
    }
}