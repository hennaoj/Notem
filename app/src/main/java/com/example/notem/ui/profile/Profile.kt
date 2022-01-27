package com.example.notem.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notem.R
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Profile(
    navController: NavController
) {
    Surface (color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            ProfileAppBar(navController = navController)
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
                    text = "Average Joe",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "joetheuser",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { navController.navigate(route = "editprofile") },
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
private fun ProfileAppBar(
    navController: NavController
) {
    TopAppBar{
        IconButton(
            onClick = {  navController.navigate(route = "home") },
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
            onClick = {},
            modifier = Modifier.fillMaxWidth(fraction = 0.3333f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
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
