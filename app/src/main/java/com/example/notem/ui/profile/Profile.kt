package com.example.notem.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
            TopAppBar {
                IconButton(
                    onClick = { navController.navigate(route = "home") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
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
                    onClick = { }
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
                    text = "User Name",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "exampleman@gmail.com",
                    color = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.height(15.dp))
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
