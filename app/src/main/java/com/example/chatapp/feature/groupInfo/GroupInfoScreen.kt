package com.example.chatapp.feature.groupInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.domain.ext.imageUrl
import com.example.chatapp.feature.newGroupChat.comp.MembersInput
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Primary
import com.streamliners.base.taskState.comp.whenLoaded

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(
    channelId: String,
    viewModel: GroupInfoViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.start(channelId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = viewModel.channel?.name ?: "Group Info") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        viewModel.channelTask.whenLoaded { channel ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AsyncImage(
                    //Todo: Try showing green dot on profile for online status
                    uri = channel.imageUrl(),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    placeholder = painterResource(id = R.drawable.ic_person)
                )

                channel.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MembersInput(viewModel.users, showCheckBox = false, userOnlineStatus = viewModel.userOnlineStatus)
                }
            }
        }

    }
}