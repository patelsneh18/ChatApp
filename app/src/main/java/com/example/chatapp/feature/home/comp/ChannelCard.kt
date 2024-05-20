package com.example.chatapp.feature.home.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.domain.model.Channel
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Neutral50

@Composable
fun ChannelCard(
    channel: Channel,
    onClick: () -> Unit
) {
    Card (
        onClick = onClick
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                uri = channel.imageUrl ?: "",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.ic_person)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

//                Text(
//                    text = channel.members.joinToString(),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Neutral50
//                )
            }
        }

    }
}