package com.example.chatapp.data

import com.example.chatapp.domain.model.User
import com.example.chatapp.helper.DataStoreUtil

class LocalRepo (
    private val dataStoreUtil: DataStoreUtil
){
    companion object {
        private const val KEY_USER = "user"
    }
    suspend fun upsertCurrentUser(user: User){
        dataStoreUtil.setData(KEY_USER, user)
    }

    suspend fun getLoggedInUser(): User {
        return getLoggedInUserNullable() ?: error("User not found in local")
    }

    suspend fun getLoggedInUserNullable(): User? {
        return dataStoreUtil.getData<User>(KEY_USER)
    }

    suspend fun isLoggedIn() = getLoggedInUserNullable() != null
}