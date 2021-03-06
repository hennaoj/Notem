package com.example.notem.ui.login

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.R
import com.example.notem.data.user.User
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.google.accompanist.insets.systemBarsPadding


@Composable
fun CreateAccount(
    navController: NavController
) {


    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        val firstName = rememberSaveable { mutableStateOf("") }
        val lastName = rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) { TopAppBar {
            IconButton(
                onClick = { navController.navigate(route = "login") },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        }}
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
            Row{
                OutlinedTextField(
                    value = firstName.value,
                    onValueChange = { data -> firstName.value = data },
                    label = { Text("first name")},
                    modifier = Modifier.fillMaxWidth(fraction = 0.47f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.1132f))
                OutlinedTextField(
                    value = lastName.value,
                    onValueChange = { data -> lastName.value = data },
                    label = { Text("last name")},
                    modifier = Modifier.fillMaxWidth(fraction = 1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = MaterialTheme.shapes.small,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                onClick = { addAccount(
                    username = username.value,
                    password = password.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    userViewModel = userViewModel,
                    navController = navController
                ) },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "create account")
            }
        }

    }
}

fun addAccount(
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    userViewModel: UserViewModel,
    navController: NavController
) {
    userViewModel.addUser(user = User(
        userName = username,
        passWord = password,
        first = firstName,
        last = lastName,
        userX = 0.toDouble(),
        userY = 0.toDouble(),
        profilePic = ""
    ))
    navController.navigate(route = "login")
}