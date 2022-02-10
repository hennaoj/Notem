package com.example.notem.ui.reminder

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
import com.example.notem.data.reminder.Reminder
import com.example.notem.data.reminder.ReminderViewModel
import com.example.notem.data.reminder.ReminderViewModelFactory
import com.google.accompanist.insets.systemBarsPadding
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditReminder(
    navController: NavController,
    reminderId: Long
) {

    var message by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }

    val context = LocalContext.current
    val reminderViewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(context.applicationContext as Application)
    )

    val reminders = reminderViewModel.readAllData.observeAsState(listOf()).value

    for (reminder in reminders) {
        if (reminder.reminderId == reminderId) {
            message = reminder.message
            reminderTime = reminder.reminderTime.formatToString()
        }
    }


    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) { TopAppBar {
            IconButton(
                onClick = { navController.navigate(route = "home") },
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
            OutlinedTextField(
                value = message,
                onValueChange = { data -> message = data },
                label = { Text("message") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = reminderTime,
                onValueChange = { data -> reminderTime = data },
                label = { Text("dd-MM-yyyy hh:mm") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { editReminder(
                    message = message,
                    reminderTime = reminderTime,
                    reminderViewModel = reminderViewModel,
                    navController = navController,
                    reminderId = reminderId)
                },
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "save reminder")
            }
        }

    }



}

fun editReminder(
    message: String,
    reminderTime: String,
    reminderViewModel: ReminderViewModel,
    navController: NavController,
    reminderId: Long
) {
    val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(reminderTime,  ParsePosition(0))
    if (date != null) {
        reminderViewModel.updateReminder(
            reminder = Reminder(
                reminderId = reminderId,
                message = message,
                locationX = 12,
                locationY = 12,
                reminderTime = date.time,
                creationTime = Date().time,
                creatorId = 2,
                sendNotification = false
            )
        )
    }
    navController.navigate(route = "home")
}


private fun Long.formatToString(): String {
    return SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault()).format(this)
}