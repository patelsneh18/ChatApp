package com.example.chatapp.feature.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapp.R
import com.example.chatapp.domain.ext.imageUrl
import com.example.chatapp.domain.ext.otherUserId
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.feature.chat.comp.MessagesList
import com.example.chatapp.helper.ext.navigateTo
import com.example.chatapp.ui.Screen
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Green
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.base.taskState.valueNullable
import com.streamliners.compose.android.comp.appBar.TitleBar
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerCropParams
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    channelId: String,
    navController: NavHostController,
    viewModel: ChatViewModel
) {
    LaunchedEffect(key1 = Unit) { viewModel.start(channelId) }

    val messageInput = remember {
        mutableStateOf(TextInputState("Message"))
    }

    val mediaPickerDialogState = rememberMediaPickerDialogState()

    val data = remember {
        derivedStateOf {
            viewModel.data.valueNullable()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            val otherUserId = data.value?.user?.id?.let {
                                data.value?.channel?.otherUserId(it)
                            }
                            if(data.value?.channel?.type == Channel.Type.OneToOne)
                                navController.navigateTo(Screen.Profile(otherUserId?: "").route)
                            else navController.navigateTo(Screen.GroupInfo(channelId).route)
                        }
                    ){
                        AsyncImage(
                            //Todo: Try showing green dot on profile for online status
                            uri = data.value?.channel?.imageUrl() ?: "",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .run {
                                    if (data.value?.isOtherUserOnline == true) {
                                        border(4.dp, Green, CircleShape)
                                    } else this
                                },
                            placeholder = painterResource(id = R.drawable.ic_person)
                        )

                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = data.value?.channel?.name ?: "Chat",
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                viewModel.data.whenLoaded { data ->
                    MessagesList(data)
                }
            }

            Row(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val scope = rememberCoroutineScope()

                IconButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        mediaPickerDialogState.value = MediaPickerDialogState.ShowMediaPicker(
                            type = MediaType.Image,
                            allowMultiple = false,
                            fromGalleryType = FromGalleryType.VisualMediaPicker,
                            cropParams = MediaPickerCropParams.Enabled()
                        ) { getList ->
                            scope.launch {
                                val list = getList()
                                list.firstOrNull()?.let {
                                    viewModel.sendImage(it.uri)
                                }
                            }
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Image"
                    )
                }
                TextInputLayout(
                    modifier = Modifier.weight(1f),
                    state = messageInput,
                    trailingIconButton = {
                        IconButton(onClick = {
                            messageInput.ifValidInput { message ->
                                viewModel.sendMessage(message) {
                                    messageInput.update("")
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Icon"
                            )
                        }
                    }
                )
            }
        }
    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.example.chatapp.fileprovider"
    )
}