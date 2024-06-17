package com.example.chatapp.feature.editProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapp.domain.model.Gender
import com.example.chatapp.domain.model.User
import com.example.chatapp.feature.editProfile.comp.ProfileImage
import com.example.chatapp.helper.launchMediaPickerDialogForImage
import com.example.chatapp.helper.navigateTo
import com.example.chatapp.ui.Screen
import com.example.chatapp.ui.comp.AddImageButton
import com.example.chatapp.ui.theme.Primary
import com.streamliners.base.taskState.comp.TaskLoadingButton
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.comp.select.RadioGroup
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.allHaveValidInputs
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.date.DatePickerDialog
import com.streamliners.pickers.date.ShowDatePicker
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.PickedMedia
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import com.streamliners.utils.DateTimeUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    email: String,
    viewModel: EditProfileViewModel,
    navController: NavHostController,
    showDatePicker: ShowDatePicker
) {
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

    var currUser: User? = null
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

    var dob by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = gender.value) {
        if (gender.value != null) genderError = false
    }


    val image = remember {
        mutableStateOf<PickedMedia?>(null)
    }

    var updateProfile by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        val lastRoute = navController.previousBackStackEntry?.destination?.route
            ?: error("no backstack entry found")
        if (lastRoute.contains("Home")) {
            updateProfile = true
            viewModel.getUserDetails(email)
        }
    }

    viewModel.getUserTask.whenLoaded { user ->
        currUser = user
        nameInput.update(user.name)
        bioInput.update(user.bio)
        gender.value = user.gender
        dob = user.dob
        if (user.profileImageUrl != null) image.value =
            PickedMedia.Image(user.profileImageUrl, null)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    val mediaPickerDialogState = rememberMediaPickerDialogState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            image.value?.let {
                ProfileImage(
                    pickedMedia = it,
                    onClick = {
                        launchMediaPickerDialogForImage(mediaPickerDialogState, scope, image)
                    },
                    Modifier.align(Alignment.CenterHorizontally)
                )
            } ?: AddImageButton(
                onClick = {
                    launchMediaPickerDialogForImage(mediaPickerDialogState, scope, image)
                },
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
                        labelExtractor = { it.name },
                        enabled = !updateProfile
                    )

                    if (genderError) Text(text = "Required!")
                }
            }

            // TODO DOB MIN MAX
            // TODO Make DOB Compulsory
            OutlinedTextField(
                value = dob ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (updateProfile) return@clickable
                        else showDatePicker(
                            DatePickerDialog.Params(
                                format = DateTimeUtils.Format.DATE_MONTH_YEAR_2,
                                prefill = dob,
                                onPicked = { date ->
                                    dob = date
                                }
                            )
                        )
                    },
                label = { Text(text = "Date of Birth") },
                enabled = false
            )

            TaskLoadingButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally),
                state = if (!updateProfile) viewModel.saveProfileTask else viewModel.updateProfileTask,
                label = if (!updateProfile) "SAVE" else "UPDATE",
                onClick = {
                    if (TextInputState.allHaveValidInputs(
                            nameInput, bioInput
                        ) && gender.value != null && !updateProfile
                    ) {
                        gender.value?.let {
                            val user = User(
                                name = nameInput.value(),
                                email = email,
                                profileImageUrl = null,
                                bio = bioInput.value(),
                                gender = it,
                                dob = dob
                            )
                            viewModel.saveUser(user, image.value) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Registration successful")
                                }
                                navController.navigateTo(
                                    Screen.Home.route,
                                    Screen.EditProfile.format()
                                )
                            }
                        }

                    } else if (updateProfile && gender.value != null) {
                        val user = currUser?.copy(
                            name = nameInput.value(),
                            bio = bioInput.value()
                        ) ?: return@TaskLoadingButton
                        viewModel.updateUser(user, image.value)

                    }
                    if (gender.value == null) genderError = true
                }
            )
        }
    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.chatapp.fileprovider"
    )
}