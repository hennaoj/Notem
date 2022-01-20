package com.example.notem.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notem.login.Login
import com.example.notem.ui.home.Home
import com.example.notem.ui.profile.Profile

@Composable
fun NotemApp(
    appState : NotemAppState = rememberNotemAppState()
) {
    NavHost(
        navController =  appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "profile") {
            Profile(navController = appState.navController)
        }
    }
}