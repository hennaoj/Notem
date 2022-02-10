package com.example.notem.ui.reminder

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.reminder.Reminder
import com.example.notem.data.reminder.ReminderViewModel
import com.example.notem.data.reminder.ReminderViewModelFactory
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.user.UserViewModelFactory
import com.google.accompanist.insets.systemBarsPadding
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddReminder(
    navController: NavController
) {

    var userId: Long = 1

    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    for (i in users.indices) {
        if (users[i].loggedIn) {
            userId = users[i].userId
        }
    }

    val reminderViewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(context.applicationContext as Application)
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        val message = rememberSaveable { mutableStateOf("") }
        val reminderTime = rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = message.value,
                onValueChange = { data -> message.value = data },
                label = { Text("reminder message")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = reminderTime.value,
                onValueChange = { data -> reminderTime.value = data },
                label = { Text("dd-mm-yyyy hh:mm")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { addReminder(
                    message = message.value,
                    reminderTime = reminderTime.value,
                    reminderViewModel = reminderViewModel,
                    navController = navController,
                    userId = userId
                ) },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "add reminder")
            }
        }

    }
}

fun addReminder(
    message: String,
    reminderTime: String,
    reminderViewModel: ReminderViewModel,
    navController: NavController,
    userId: Long
) {
    val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(reminderTime,  ParsePosition(0))
    if (date != null) {
        reminderViewModel.addReminder(
            reminder = Reminder(
                message = message,
                locationX = 12,
                locationY = 12,
                reminderTime = date.time,
                creationTime = Date().time,
                creatorId = userId,
                sendNotification = false
            )
        )
    }
    navController.navigate(route = "home")
}