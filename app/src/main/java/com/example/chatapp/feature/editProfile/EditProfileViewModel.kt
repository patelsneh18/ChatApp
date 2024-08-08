package com.example.chatapp.feature.editProfile

import androidx.core.net.toUri
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.FirestoreCollections.usersColl
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.model.User
import com.example.chatapp.ui.comp.ImageState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
): BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()
    var currentUser: User? = null

    fun saveUser(
        user: User,
        image: ImageState,
        onSuccess: () -> Unit
    ){
        execute(showLoadingDialog = false){
            saveProfileTask.load {

                val token = Firebase.messaging.token.await()

                val userId = currentUser?.id ?: Firebase.firestore.usersColl().document().id

                var updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(userId, image),
                    fcmToken = token,
                    id = userId
                )

                updatedUser = userRepo.upsertUser(updatedUser)
                localRepo.upsertCurrentUser(updatedUser)
                executeOnMain { onSuccess() }
            }
        }
    }

    private suspend fun uploadProfileImage(userId: String, imageState: ImageState): String? {
        return when(imageState) {
            ImageState.Empty -> null
            is ImageState.Exists -> imageState.url
            is ImageState.New -> {
                storageRepo.saveFile(
                    path = "profileImages/$userId.jpg",
                    uri = imageState.pickedMedia.uri.toUri()
                )
            }
        }
    }

    suspend fun getUserDetails(
        onSuccess: (User) -> Unit
    ) {
        execute {
            localRepo.getLoggedInUserNullable()?.let { user ->
                currentUser = user
                onSuccess(user)
            }
        }
    }
}