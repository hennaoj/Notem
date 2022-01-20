package com.example.notem.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Home(
    navController: NavController
) {
    Surface (color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { navController.navigate(route = "profile") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                }
            }
        }
    }
}