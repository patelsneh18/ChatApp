package com.example.chatapp.feature.chat.comp

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.domain.model.Message
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Neutral50
import com.streamliners.utils.DateTimeUtils
import com.streamliners.utils.DateTimeUtils.Format.*
import com.streamliners.utils.DateTimeUtils.formatTime

@Composable
fun MessageCard(
    message: Message,
    time: String
) {
    Card {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                color = Neutral50
            )
        }
    }
}