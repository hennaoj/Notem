package com.example.notem.ui.profile

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.notem.data.user.User
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.google.accompanist.insets.systemBarsPadding
import java.io.File

@Composable
fun EditProfile(
    navController: NavController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    val userLat =  remember { mutableStateOf(0.toDouble())}
    val userLon =  remember { mutableStateOf(0.toDouble()) }
    var id: Long = 0

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    for (i in users.indices) {
        if (users[i].loggedIn) {
            firstName = users[i].first
            lastName = users[i].last
            username = users[i].userName
            id = users[i].userId
            passWord = users[i].passWord
            profilePic = users[i].profilePic
            userLat.value = users[i].userY
            userLon.value = users[i].userX
        }
    }

    val imgFile = File(profilePic)

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
            if (profilePic != "") {
                Image(
                    painter = rememberImagePainter(imgFile),
                    contentDescription = "...",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    modifier = Modifier.size(200.dp),
                    contentDescription = "profile picture",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { navController.navigate(route = "camera")}
            ) {
                Text(text = "Change")
            }
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
                        navController = navController,
                        profilePic = profilePic,
                        userLat = userLat.value,
                        userLon = userLon.value
                    )
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
    navController: NavController,
    profilePic: String,
    userLon: Double,
    userLat: Double
) {
    viewModel.updateUser(user = User(
        userId = id,
        userName = username,
        first = first,
        last = last,
        passWord = password,
        loggedIn = true,
        userX = userLon,
        userY = userLat,
        profilePic = profilePic
    ))
    navController.navigate(route = "profile")
}