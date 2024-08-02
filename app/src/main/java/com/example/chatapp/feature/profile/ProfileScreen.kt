package com.example.chatapp.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
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
import com.example.chatapp.domain.ext.profileImageUrl
import com.example.chatapp.feature.profile.comp.IconText
import com.example.chatapp.ui.general.AsyncImage
import com.example.chatapp.ui.theme.Primary
import com.streamliners.base.taskState.comp.whenLoaded

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    viewModel: ProfileViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start(userId)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = viewModel.user?.name ?: "Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        viewModel.userTask.whenLoaded { user->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp, 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AsyncImage(
                    //Todo: Try showing green dot on profile for online status
                    uri = user.profileImageUrl(),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    placeholder = painterResource(id = R.drawable.ic_person)
                )
                
                Text(text = user.bio, style = MaterialTheme.typography.bodyMedium)

                IconText(imageVector = Icons.Default.Email, text = user.email)
                IconText(imageVector = Icons.Default.Person, text = user.name)
                IconText(imageVector = Icons.Default.CalendarToday, text = user.dob ?: "1999")

            }
        }

    }
}