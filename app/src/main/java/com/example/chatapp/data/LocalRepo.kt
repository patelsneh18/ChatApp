package com.example.chatapp.data

import com.example.chatapp.helper.DataStoreUtil

class LocalRepo (
    val dataStoreUtil: DataStoreUtil
){
    suspend fun onLoggedIn(){
        dataStoreUtil.setData("isLoggedIn", true)
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStoreUtil.getData<Boolean>("isLoggedIn") ?: false
    }
}