package com.example.notem.ui.profile

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.User
import com.example.notem.data.UserViewModel
import com.example.notem.data.UserViewModelFactory
import com.google.accompanist.insets.systemBarsPadding





@Composable
fun EditProfile(
    navController: NavController
) {var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var id: Long = 0

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
            id = users[i].userId
            passWord = users[i].passWord
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) { TopAppBar {
            IconButton(
                onClick = { navController.navigate(route = "profile") },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Row{
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("first name")},
                    modifier = Modifier.fillMaxWidth(fraction = 0.47f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.1132f))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { data -> lastName = data },
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
                value = username,
                onValueChange = { data -> username = data },
                label = { Text("username")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    saveProfile(
                        first = firstName,
                        last = lastName,
                        username = username,
                        viewModel = userViewModel,
                        id = id,
                        password = passWord,
                        navController = navController)
                },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "save profile")
            }
        }

    }
}


fun saveProfile(
    first: String,
    last: String,
    username: String,
    password: String,
    viewModel: UserViewModel,
    id: Long,
    navController: NavController
) {
    viewModel.updateUser(user = User(userId = id, userName = username, first = first, last = last,
        passWord = password, loggedIn = true))
    navController.navigate(route = "profile")
}