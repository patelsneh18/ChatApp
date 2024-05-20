package com.example.chatapp.feature.chat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.CenterText

@Composable
fun ChatScreen(
    channelId: String,
    navController: NavHostController
) {
    TitleBarScaffold(
        title = "Chat",
        navigateUp = { navController.navigateUp() }
    ) {
        CenterText(text = channelId, style = MaterialTheme.typography.titleLarge)
    }
}