package com.example.chatapp.feature.editProfile.comp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.streamliners.pickers.media.PickedMedia

@Composable
fun ProfileImage(
    pickedMedia: PickedMedia,
    onClick : () -> Unit,
    modifier: Modifier
    ) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pickedMedia.uri)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = modifier.clip(CircleShape)
            .size(100.dp)
            .clickable { onClick() }

    )

}