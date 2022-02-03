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
fun SecondHelpScreen(
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
            SecondHelpContent()
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.navigate(route = "help3") },
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
fun SecondHelpContent(
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
                text = "You can add a title, a date and an optional location to your reminder.",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(
                    end = 24.dp,
                    start = 24.dp,
                    bottom = 20.dp,
                    top = 20.dp
                )
            )
            Form()
        }
    }
}

@Composable
private fun Form() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("title")},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            shape = MaterialTheme.shapes.small,
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("date")},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            shape = MaterialTheme.shapes.small,
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("location")},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            shape = MaterialTheme.shapes.small,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {  },
            enabled = true,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text(text = "add reminder")
        }
    }
}

@Composable
private fun HomeAppBar(
) {
    TopAppBar{
        IconButton(
            onClick = {  },
            enabled = false
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}