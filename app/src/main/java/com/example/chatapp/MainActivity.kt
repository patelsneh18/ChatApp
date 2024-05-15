package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.login.LoginScreen
import com.example.chatapp.feature.splash.SplashScreen
import com.example.chatapp.ui.ChatAppNavHost
import com.example.chatapp.ui.Screen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.BuildConfig
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
    }


}
