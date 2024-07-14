package com.example.chatapp.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.chatapp.BuildConfig
import com.example.chatapp.data.LocalRepo
import com.example.chatapp.data.temp.Scripts
import com.example.chatapp.domain.Constants
import com.example.chatapp.domain.usecase.LastOnlineTSUpdater
import com.example.chatapp.ui.ChatAppNavHost
import com.example.chatapp.ui.theme.ChatAppTheme
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    override var buildType: String = BuildConfig.BUILD_TYPE

    internal val lastOnlineTSUpdater: LastOnlineTSUpdater by inject()
    internal val localRepo: LocalRepo by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavHost()
                    UiEventDialogs()
                }
            }
        }
    }

    internal lateinit var onlineTSUpdaterJob: Job

    override fun onPause() {
        super.onPause()
        if (::onlineTSUpdaterJob.isInitialized) {
            onlineTSUpdaterJob.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        initiateOnlineTSUpdater()
    }
    private fun runScripts() {
        execute {
            Scripts.saveDummyUsers()
        }
    }
}
