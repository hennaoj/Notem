package com.example.notem.ui.profile

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.user.UserViewModelFactory
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Profile(
    navController: NavController
) {

    var firstName = ""
    var lastName = ""
    var username = ""

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    for (i in users.indices) {
        if (users[i].loggedIn) {
            firstName = users[i].first
            lastName = users[i].last
            username = users[i].userName
        }
    }

    Surface (color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            HomeAppBar(navController = navController)
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    modifier = Modifier.size(200.dp),
                    contentDescription = "profile picture",
                    tint = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "$firstName $lastName",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = username,
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { navController.navigate(route = "editProfile") },
                    enabled = true,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(text = "edit profile")
                }
            }
        }

    }
}

@Composable
private fun HomeAppBar(
    navController: NavController
) {
    TopAppBar{
        IconButton(
            onClick = { navController.navigate(route = "home") },
            modifier = Modifier.fillMaxWidth(fraction = 0.2f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 0.25f)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "profile") },
            modifier = Modifier.fillMaxWidth(fraction = 0.3333f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "help") },
            modifier = Modifier.fillMaxWidth(fraction = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "login") },
            modifier = Modifier.fillMaxWidth(fraction = 1f)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null
            )
        }
    }
}
