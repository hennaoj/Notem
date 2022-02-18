package com.example.notem.ui.login

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
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
fun Login(
    navController: NavController
) {


    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    //logging out all users when entering login screen
    for (i in users.indices) {
        if (users[i].loggedIn) {
            userViewModel.logUser(loggedIn = false, id = users[i].userId)
        }
    }

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
                "application icon",
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
                onClick = { checkDetails(
                    username = username.value,
                    password = password.value,
                    navController = navController,
                    users = users,
                    viewModel = userViewModel
                ) },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "login")
            }
            ClickableText(
                text = AnnotatedString(text = "Create an account"),
                onClick = { navController.navigate(route = "createAccount") },
                modifier = Modifier.padding(20.dp)
            )
        }

    }
}

fun checkDetails(
    username: String,
    password: String,
    navController: NavController,
    users: List<User>,
    viewModel: UserViewModel
) {
    for (i in users.indices) {
        if (username == users[i].userName && password == users[i].passWord) {
            viewModel.logUser(loggedIn = true, id = users[i].userId)
            navController.navigate(route = "home")
        }
    }
}
