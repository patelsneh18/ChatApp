package com.example.chatapp.helper

import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.UserRepo
import com.example.chatapp.feature.editProfile.EditProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val appModule = module {
    single { UserRepo() }
    single { LocalRepo(DataStoreUtil.create(get())) }
}

val viewModelModule = module{
    viewModel { EditProfileViewModel(get(), get()) }
}