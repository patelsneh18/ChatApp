package com.example.chatapp.feature.newGroupChat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.feature.newGroupChat.comp.GroupInfoInput
import com.example.chatapp.feature.newGroupChat.comp.MembersInput
import com.example.chatapp.helper.navigateTo
import com.example.chatapp.ui.Screen
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBar
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.nullableValue
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.media.PickedMedia

@Composable
fun NewGroupChatScreen(
    viewModel: NewGroupChatViewModel,
    navController: NavController
) {

    val image = remember {
        mutableStateOf<PickedMedia?>(null)
    }

    val nameInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Name",
                inputConfig = InputConfig.text {
                    minLength = 3
                    maxLength = 25
                }
            )
        )
    }

    val descriptionInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Description",
                inputConfig = InputConfig.text {
                    optional = true
                }
            )
        )
    }

    val members = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(key1 = 1) {
        viewModel.start()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "New Chat",
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                nameInput.ifValidInput { name ->
                    viewModel.createGroupChannel(
                        name, descriptionInput.nullableValue(), image.value, members
                    ) { channelId ->
                        navController.navigateTo(Screen.Chat(channelId), Screen.NewGroupChat)
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Group, contentDescription = "Create Group")
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = "Create")
            }
        }
    ) { paddingValues ->
        //TODO: Avoid hiding entire screen when members are loading
        viewModel.userListTask.whenLoaded { usersList ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 72.dp)
            ) {
                item {
                    GroupInfoInput(nameInput, descriptionInput, image)
                }

                MembersInput(usersList, members)
            }
        }
    }

}
