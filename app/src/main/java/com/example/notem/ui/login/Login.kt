package com.example.notem.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notem.R
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Login(
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.notem),
                "content description",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data },
                label = { Text("username")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                label = { Text("password")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation(),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {  navController.navigate(route = "home") },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "login")
            }
        }

    }
}
