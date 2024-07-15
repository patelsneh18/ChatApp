package com.example.chatapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.comp.CenterText
import com.streamliners.helpers.NotificationHelper

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
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("showDetails", true)
                        navController.navigate(
                            Screen.EditProfile(
                                Firebase.auth.currentUser?.email ?: error("No email found")
                            ).route
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "EditProfile",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column (
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.NewGroupChat.route) }
                ) {
                    Icon(imageVector = Icons.Default.Group, contentDescription = "New group chat")
                }

                FloatingActionButton(
                    onClick = { navController.navigate(Screen.NewChat.route) }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "New one to one chat")
                }
            }
        }
    ) { paddingValues ->

        viewModel.state.whenLoaded { data ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (data.channels.isEmpty() ) {
                    item { 
                        CenterText(text = "No Chats Found")
                    }
                } else {
                    items(data.channels) { channel ->
                            ChannelCard(
                                channel = channel,
                                isOnline = viewModel.isChannelOneToOneAndOnline(channel)
                            ) {
                            navController.navigate(Screen.Chat(channel.id()).route)
                        }
                    }
                }
            }
        }
    }

    NotificationHelper.PermissionsSetup()
}

