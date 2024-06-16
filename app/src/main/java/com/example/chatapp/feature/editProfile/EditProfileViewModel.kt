package com.example.chatapp.feature.editProfile

import android.util.Log
import androidx.core.net.toUri
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.pickers.media.PickedMedia
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.exp

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
): BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()
    val updateProfileTask = taskStateOf<Unit>()
    val getUserTask = taskStateOf<User>()
    fun saveUser(user: User, image: PickedMedia?, onSuccess: () -> Unit){
        execute(showLoadingDialog = false){
            saveProfileTask.load {
                val token = Firebase.messaging.token.await()

                val updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(user.email, image),
                    fcmToken = token
                )

                userRepo.saveUser(updatedUser)
                val savedUser = userRepo.getUserWithEmail(user.email) ?: error("No user found")
                localRepo.upsertCurrentUser(savedUser)
                executeOnMain { onSuccess() }
            }
        }
    }

    fun updateUser(user: User, image: PickedMedia?) {
        execute {
            updateProfileTask.load {
                if (image?.uri?.split(":")?.get(0)?.contains("content") == true) {
                    val updatedUser = user.copy(
                        profileImageUrl = uploadProfileImage(user.email, image)
                    )
                    userRepo.upsertUser(user)
                    localRepo.upsertCurrentUser(updatedUser)
                } else {
                    userRepo.upsertUser(user)
                    localRepo.upsertCurrentUser(user)
                }
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

    suspend fun getUserDetails(email: String) {
        execute {
            getUserTask.load {
                userRepo.getUserWithEmail(email) ?: error("No user found")
            }
        }
    }
}