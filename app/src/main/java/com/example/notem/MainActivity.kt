package com.example.notem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.notem.ui.NotemApp
import com.example.notem.ui.theme.NotemTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            NotemTheme {
                // A surface container using the 'background' color from the theme
                Surface( color = MaterialTheme.colors.background ) {
                    NotemApp()
                }
            }
        }


    }



}