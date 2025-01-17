package com.example.chatapp.helper

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.feature.chat.ChatViewModel
import com.example.chatapp.feature.editProfile.EditProfileViewModel
import com.example.chatapp.feature.home.HomeViewModel
import com.example.chatapp.feature.login.LoginViewModel
import com.example.chatapp.feature.newChat.NewChatViewModel
import com.example.chatapp.feature.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { UserRepo() }
    single { LocalRepo(DataStoreUtil.create(get())) }
    single { StorageRepo() }
    single { ChannelRepo() }
}

val viewModelModule = module{
    viewModel { EditProfileViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { NewChatViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ChatViewModel(get(), get(), get()) }
}