package com.example.notem.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notem.ui.help.HelpScreen
import com.example.notem.ui.help.SecondHelpScreen
import com.example.notem.ui.help.ThirdHelpScreen
import com.example.notem.ui.home.Home
import com.example.notem.ui.login.CreateAccount
import com.example.notem.ui.profile.EditProfile
import com.example.notem.ui.profile.Profile
import com.example.notem.ui.login.Login
import com.example.notem.ui.reminder.AddReminder
import com.example.notem.ui.reminder.EditReminder

@Composable
fun NotemApp(
) {
    val appState : NotemAppState = rememberNotemAppState()
    NavHost(
        navController =  appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "createAccount") {
            CreateAccount(navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "profile") {
            Profile(navController = appState.navController)
        }
        composable(route = "addReminder") {
            AddReminder(navController = appState.navController)
        }
        composable(route = "editProfile") {
            EditProfile(navController = appState.navController)
        }
        composable(route = "help") {
            HelpScreen(navController = appState.navController)
        }
        composable(route = "help2") {
            SecondHelpScreen(navController = appState.navController)
        }
        composable(route = "help3") {
            ThirdHelpScreen(navController = appState.navController)
        }
        composable(route = "addReminder") {
            AddReminder(navController = appState.navController)
        }
        composable(
            route = "editReminder/{reminderId}",
            arguments = listOf(
                navArgument("reminderId") {
                    type = NavType.LongType
                }
            )
        ) {
            EditReminder(navController = appState.navController, reminderId = it.arguments?.getLong("reminderId")!!)
        }
    }
}