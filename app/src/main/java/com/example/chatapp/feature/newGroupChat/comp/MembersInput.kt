package com.example.chatapp.feature.newGroupChat.comp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.domain.ext.id
import com.example.chatapp.domain.model.User
import com.example.chatapp.ui.comp.UserCard

fun LazyListScope.MembersInput(
    usersList: List<User>,
    members: SnapshotStateList<String> = mutableStateListOf(),
    showCheckBox: Boolean = true,
    userOnlineStatus: MutableState<Map<String, Boolean>> = mutableStateOf(emptyMap())
) {
    item {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 8.dp)
                .padding(vertical = 10.dp),
            text = "Members",
            style = MaterialTheme.typography.titleMedium
        )
    }

    items(usersList) { user ->
        UserCard(
            modifier = Modifier.padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            user = user,
            onCheckedChanged = { checked ->
                if (checked) members.add(user.id())
                else members.remove(user.id())
            },
            onClick = {
                if(members.contains(user.id())) members.remove(user.id())
                else members.add(user.id())
            },
            checked = members.contains(user.id()),
            userOnlineStatus = userOnlineStatus.value[user.id()] ?: false,
            showCheckBox = showCheckBox
        )
    }
}