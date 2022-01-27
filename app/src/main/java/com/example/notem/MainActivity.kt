package com.example.notem

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.notem.ui.NotemApp
import com.example.notem.ui.NotemAppState
import com.example.notem.ui.theme.NotemTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = UserPreferences(this)
        val username = prefs.getUsername()
        val password = prefs.getPassword()

        super.onCreate(savedInstanceState)
        setContent {
            NotemTheme {
                // A surface container using the 'background' color from the theme
                Surface( color = MaterialTheme.colors.background ) {
                    NotemApp(username, password)
                }
            }
        }


    }



}