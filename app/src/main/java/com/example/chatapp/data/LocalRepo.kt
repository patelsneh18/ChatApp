package com.example.chatapp.data

import com.example.chatapp.domain.model.User
import com.example.chatapp.helper.DataStoreUtil

class LocalRepo (
    val dataStoreUtil: DataStoreUtil
){
    suspend fun onLoggedIn(user: User){
        dataStoreUtil.setData("user", user)
    }

    suspend fun getLoggedInUser(): User {
        return getLoggedInUserNullable() ?: error("User not found in local")
    }

    private suspend fun getLoggedInUserNullable(): User? {
        return dataStoreUtil.getData<User>("user")
    }

    suspend fun isLoggedIn() = getLoggedInUserNullable() != null
}