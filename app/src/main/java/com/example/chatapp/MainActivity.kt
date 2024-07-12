package com.example.chatapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.chatapp.data.temp.Scripts
import com.example.chatapp.ui.ChatAppNavHost
import com.example.chatapp.ui.theme.ChatAppTheme
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs

class MainActivity : BaseActivity() {
    override var buildType: String = BuildConfig.BUILD_TYPE
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

//        runScripts()
    }

    private fun runScripts() {
        execute {
            Scripts.saveDummyUsers()
        }
    }
}
