package com.example.notem.ui.reminder

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
        }}
        val message = rememberSaveable { mutableStateOf("") }
        val reminderTime = rememberSaveable { mutableStateOf("") }
        val icon = rememberSaveable { mutableStateOf("Default")}
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconListDropdown(reminderIcon = icon)
            Spacer(modifier = Modifier.height(10.dp))
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
                    userId = userId,
                    icon = icon.value
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
    userId: Long,
    icon: String
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
                sendNotification = false,
                icon = icon
            )
        )
    }
    navController.navigate(route = "home")
}

@Composable
private fun IconListDropdown(
    reminderIcon: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp
    } else {
        Icons.Filled.ArrowDropDown
    }

    val icons = listOf(
        Icons.Filled.StickyNote2,
        Icons.Filled.Work,
        Icons.Filled.MedicalServices,
        Icons.Filled.Paid,
        Icons.Filled.Event,
        Icons.Filled.School
    )

    val iconLabels = listOf(
        "Default",
        "Work",
        "Medical",
        "Finances",
        "Event",
        "School"
    )

    Column {
        OutlinedTextField(
            value = reminderIcon.value,
            onValueChange = { reminderIcon.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Icon")},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in icons.indices) {
                DropdownMenuItem(
                    onClick = {
                        reminderIcon.value = iconLabels[i]
                        expanded = false
                    }
                ) {
                    Icon(
                        imageVector = icons[i],
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = iconLabels[i])
                }
            }
        }
    }
}