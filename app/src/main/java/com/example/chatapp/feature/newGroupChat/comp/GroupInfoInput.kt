package com.example.chatapp.feature.newGroupChat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.feature.editProfile.comp.ProfileImage
import com.example.chatapp.helper.launchMediaPickerDialogForImage
import com.example.chatapp.ui.comp.AddImageButton
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.PickedMedia
import com.streamliners.pickers.media.rememberMediaPickerDialogState

@Composable
fun GroupInfoInput(
    nameInput: MutableState<TextInputState>,
    descriptionInput: MutableState<TextInputState>,
    image: MutableState<PickedMedia?>
) {

    val mediaPickerDialogState = rememberMediaPickerDialogState()
    val scope = rememberCoroutineScope()


    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        image.value?.let {
            ProfileImage(
                data = it.uri,
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
        TextInputLayout(state = descriptionInput)
    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.chatapp.fileprovider"
    )
}