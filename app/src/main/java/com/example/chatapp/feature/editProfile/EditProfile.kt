package com.example.chatapp.feature.editProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapp.domain.model.Gender
import com.example.chatapp.domain.model.User
import com.example.chatapp.feature.editProfile.comp.AddImageButton
import com.example.chatapp.feature.editProfile.comp.ProfileImage
import com.example.chatapp.ui.Screen
import com.streamliners.compose.comp.select.RadioGroup
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.allHaveValidInputs
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.PickedMedia
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    email: String,
    viewModel: EditProfileViewModel,
    navController: NavHostController
) {

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    val mediaPickerDialogState = rememberMediaPickerDialogState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->

        val nameInput = remember {
            mutableStateOf(
                TextInputState(
                    label = "Name",
                    inputConfig = InputConfig.text {
                        minLength = 3
                        maxLength = 25
                    }
                )
            )
        }

        val bioInput = remember {
            mutableStateOf(
                TextInputState(
                    label = "Bio",
                    inputConfig = InputConfig.text {
                        minLength = 5
                        maxLength = 50
                    }
                )
            )
        }

        val gender = remember {
            mutableStateOf<Gender?>(null)
        }

        var genderError by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = gender.value) {
            if (gender.value != null) genderError = false
        }


        val image = remember {
            mutableStateOf<PickedMedia?>(null)
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val initImagePicker = {
                mediaPickerDialogState.value = MediaPickerDialogState.Visible(
                    type = MediaType.Image,
                    allowMultiple = false,
                    fromGalleryType = FromGalleryType.VisualMediaPicker
                ) {getList ->
                    scope.launch {
                        val list = getList()
                        list.firstOrNull()?.let {
                            image.value = it
                        }
                    }
                }
            }
            image.value?.let {
                ProfileImage(
                    pickedMedia = it,
                    initImagePicker,
                    Modifier.align(Alignment.CenterHorizontally))
            } ?: AddImageButton (
                initImagePicker,
                Modifier.align(Alignment.CenterHorizontally)
            )

            TextInputLayout(state = nameInput)

            OutlinedTextField(
                value = email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                enabled = false
            )

            TextInputLayout(state = bioInput)

            Card {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    RadioGroup(
                        title = "Gender",
                        state = gender,
                        options = Gender.entries.toList(),
                        labelExtractor = { it.name }
                    )

                    if (genderError) Text(text = "Required!")
                }
            }

            Button(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    if(TextInputState.allHaveValidInputs(
                        nameInput, bioInput
                    ) && gender.value != null) {

                        gender.value?.let {
                            val user = User(
                                name = nameInput.value(),
                                email = email,
                                profileImageUrl = null,
                                bio = bioInput.value(),
                                gender = it
                            )
                            viewModel.saveUser(user, image.value) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Registration successful")
                                }
                                navController.navigate(Screen.Home.route)
                            }
                        }

                    }
                    if (gender.value == null) genderError = true
                }
            ) {
                Text(text = "Save")
            }
        }
    }

    MediaPickerDialog(state = mediaPickerDialogState, authority = "com.example.chatapp.fileprovider")
}