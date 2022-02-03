package com.example.notem.ui.help

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun ThirdHelpScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, top = 60.dp, end = 40.dp, start = 40.dp)
        ) {
            ThirdHelpContent()
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.navigate(route = "home") },
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ThirdHelpContent(
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            HomeAppBar()
            Text(
                text = "Through the profile screen, you can edit your user information such as the username and password.",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(
                    end = 24.dp,
                    start = 24.dp,
                    bottom = 20.dp,
                    top = 20.dp
                )
            )
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
                    text = "Joe Average",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "username",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { },
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
) {
    TopAppBar{
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 0.2f),
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null

            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 0.25f),
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(fraction = 0.3333f),
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 0.5f),
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(fraction = 1f),
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null
            )
        }
    }
}