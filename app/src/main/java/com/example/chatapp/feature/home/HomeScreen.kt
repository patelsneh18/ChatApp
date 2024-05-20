package com.example.chatapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.domain.ext.id
import com.example.chatapp.feature.home.comp.ChannelCard
import com.example.chatapp.ui.Screen
import com.example.chatapp.ui.theme.Primary
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.comp.CenterText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome to ChatApp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.NewChat.route) }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { paddingValues ->

        viewModel.channelsState.whenLoaded { channels ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (channels.isEmpty() ) {
                    item { 
                        CenterText(text = "No Chats Found")
                    }
                } else {
                    items(channels) { channel ->
                        ChannelCard(channel = channel) {
                            navController.navigate(Screen.Chat(channel.id()).route)
                        }
                    }
                }
            }
        }
    }
}

