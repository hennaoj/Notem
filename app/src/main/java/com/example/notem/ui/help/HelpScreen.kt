package com.example.notem.ui.help

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun HelpScreen(
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
            HelpContent()
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.navigate(route = "help2") },
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
fun HelpContent(
) {
    Scaffold(
        floatingActionButton = { FloatingActionButton(
            onClick = {},
            modifier = Modifier.padding(all = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            HomeAppBar()
            Text(
                text = "You can add reminders in the home screen by pressing the button on the bottom right.",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(
                    end = 24.dp,
                    start = 24.dp,
                    bottom = 20.dp,
                    top = 20.dp
                )
            )
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
                contentDescription = null,
                tint = Color.White
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
                contentDescription = null
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
