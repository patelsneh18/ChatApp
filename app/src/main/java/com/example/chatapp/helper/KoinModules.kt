package com.example.chatapp.helper

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.remote.ChannelRepo
import com.example.chatapp.data.remote.OtherRepo
import com.example.chatapp.data.remote.StorageRepo
import com.example.chatapp.data.remote.UserRepo
import com.example.chatapp.domain.usecase.LastOnlineTSFetcher
import com.example.chatapp.domain.usecase.LastOnlineTSUpdater
import com.example.chatapp.domain.usecase.NewMessageNotifier
import com.example.chatapp.feature.chat.ChatViewModel
import com.example.chatapp.feature.editProfile.EditProfileViewModel
import com.example.chatapp.feature.groupInfo.GroupInfoViewModel
import com.example.chatapp.feature.home.HomeViewModel
import com.example.chatapp.feature.login.LoginViewModel
import com.example.chatapp.feature.newChat.NewChatViewModel
import com.example.chatapp.feature.newGroupChat.NewGroupChatViewModel
import com.example.chatapp.feature.profile.ProfileViewModel
import com.example.chatapp.feature.splash.SplashViewModel
import com.example.chatapp.helper.fcm.FcmSender
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { UserRepo() }
    single { LocalRepo(DataStoreUtil.create(get())) }
    single { StorageRepo() }
    single { ChannelRepo() }
    single { OtherRepo() }

    single { HttpClient(CIO) { expectSuccess = true } }
    single { FcmSender(get()) }
    single { NewMessageNotifier(get(), get(), get()) }
    single { LastOnlineTSUpdater(get(), get()) }
    single { LastOnlineTSFetcher(get()) }
}

val viewModelModule = module{
    viewModel { EditProfileViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { NewChatViewModel(get(), get(), get()) }
    viewModel { NewGroupChatViewModel(get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { ChatViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { GroupInfoViewModel(get(), get(), get()) }
}