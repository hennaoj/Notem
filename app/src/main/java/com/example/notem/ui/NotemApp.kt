package com.example.notem.ui

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.example.notem.ui.camera.CameraView
import com.example.notem.ui.help.HelpScreen
import com.example.notem.ui.help.SecondHelpScreen
import com.example.notem.ui.help.ThirdHelpScreen
import com.example.notem.ui.home.Home
import com.example.notem.ui.login.CreateAccount
import com.example.notem.ui.profile.EditProfile
import com.example.notem.ui.profile.Profile
import com.example.notem.ui.login.Login
import com.example.notem.ui.maps.AddLocation
import com.example.notem.ui.maps.UserLocation
import com.example.notem.ui.reminder.AddReminder
import com.example.notem.ui.reminder.EditReminder
import java.io.File
import java.util.concurrent.Executor

@Composable
fun NotemApp(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit
) {
    val appState : NotemAppState = rememberNotemAppState()
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value
    var destination = ""


    for (i in users.indices) {
        if (users[i].loggedIn) {
            destination = "home"
        }
    }

    if (destination != "home") {
        destination = "login"
    }

    NavHost(
        navController =  appState.navController,
        startDestination = destination
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
        composable(route = "addLocation") {
            AddLocation(navController = appState.navController)
        }
        composable(route = "userLocation") {
            UserLocation(navController = appState.navController)
        }
        composable(route = "camera") {
            CameraView(
                outputDirectory = outputDirectory,
                executor = executor,
                onImageCaptured = onImageCaptured,
                onError = { Log.e("kilo", "View error:", it) },
                navController = appState.navController
            )
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