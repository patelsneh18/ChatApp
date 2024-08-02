package com.example.chatapp.feature.profile.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Icon(imageVector = imageVector, contentDescription = "Icon")
        
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}