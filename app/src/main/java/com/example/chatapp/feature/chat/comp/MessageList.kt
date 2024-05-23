package com.example.chatapp.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.feature.chat.ChatViewModel

@Composable
fun MessagesList(data: ChatViewModel.Data) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data.channel.messages) { message ->
            val selfSent = data.user.id() == message.sender
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (selfSent) Alignment.CenterEnd else Alignment.CenterStart) {
                MessageCard(
                    message = message
                )
            }
        }
    }
}