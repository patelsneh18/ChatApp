package com.example.chatapp.ui.comp

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.domain.model.Gender
import com.example.chatapp.domain.model.User
import com.example.chatapp.helper.userInitialBasedProfileImage
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Neutral50

@Composable
fun UserCard(
    user: User
) {
    Card {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                uri = user.profileImageUrl ?: userInitialBasedProfileImage(user.name),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(42.dp),
                placeholder = painterResource(id = R.drawable.ic_person)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral50
                )
            }
        }
    }
}

@Preview
@Composable
fun UserPreview() {
    UserCard(
        user = User(
            id = null,
            name = "Sneh",
            email = "patelsneh18@gmail.com",
            profileImageUrl = null,
            bio = "BIooooooooooo",
            gender = Gender.Male,
            dob = "18/01/2002"
        )
    )
}