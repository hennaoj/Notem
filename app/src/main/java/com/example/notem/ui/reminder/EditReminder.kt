package com.example.notem.ui.reminder

import android.annotation.SuppressLint
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
import com.example.notem.data.viewModelProviderFactoryOf
import com.google.accompanist.insets.systemBarsPadding
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun EditReminder(
    navController: NavController,
    reminderId: Long
) {

    var message by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var creator: Long by remember { mutableStateOf(1) }
    val icon = rememberSaveable { mutableStateOf("Default")}
    var checkedState by remember { mutableStateOf(true) }
    var creationTime by remember { mutableStateOf(0.toLong())}
    val notificationTime = rememberSaveable { mutableStateOf(0.toLong())}
    var reminderLat by remember { mutableStateOf(0.toDouble()) }
    var reminderLon by remember { mutableStateOf(0.toDouble()) }


    val context = LocalContext.current
    val reminderViewModel: ReminderViewModel = viewModel(
        factory =viewModelProviderFactoryOf { ReminderViewModel(context.applicationContext as Application) }
    )

    val reminders = reminderViewModel.readAllData.observeAsState(listOf()).value

    //looking for the correct reminder given the reminderId parameter
    for (reminder in reminders) {
        if (reminder.reminderId == reminderId) {
            message = reminder.message
            reminderTime = reminder.reminderTime.formatToString()
            icon.value = reminder.icon
            creator = reminder.creatorId
            checkedState = reminder.sendNotification
            creationTime = reminder.creationTime
            notificationTime.value = reminder.reminderTime
            reminderLat = reminder.locationY
            reminderLon = reminder.locationX
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
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
            IconListDropdown(reminderIcon = icon)
            Spacer(modifier = Modifier.height(10.dp))
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
            if (reminderTime != SimpleDateFormat("dd-MM-yyyy HH:mm").parse(
                    "01-01-1970 00:00", ParsePosition(0)).time.formatToString()) {
                OutlinedTextField(
                    value = reminderTime,
                    onValueChange = { data -> reminderTime = data },
                    label = { Text("dd-mm-yyyy hh:mm") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = MaterialTheme.shapes.small,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row {
                FloatingActionButton(
                    onClick = {
                        deleteReminder(
                            navController = navController,
                            reminderViewModel = reminderViewModel,
                            reminderId = reminderId,
                            creationTime = creationTime,
                            notificationTime = notificationTime.value
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.5f))
                Button(
                    onClick = {
                        editReminder(
                            message = message,
                            reminderTime = reminderTime,
                            reminderViewModel = reminderViewModel,
                            navController = navController,
                            reminderId = reminderId,
                            icon = icon.value,
                            creatorId = creator,
                            notification = checkedState,
                            creationTime = creationTime,
                            notificationTime = notificationTime.value,
                            reminderLat = reminderLat,
                            reminderLon = reminderLon
                        )
                    },
                    modifier = Modifier.fillMaxWidth(fraction = 1f).padding(top = 10.dp),
                    enabled = true,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(text = "save reminder")
                }
            }
        }
    }

}

fun editReminder(
    message: String,
    reminderTime: String,
    reminderViewModel: ReminderViewModel,
    navController: NavController,
    reminderId: Long,
    icon: String,
    creatorId: Long,
    notification: Boolean,
    creationTime: Long,
    notificationTime: Long,
    reminderLon: Double,
    reminderLat: Double
) {
    val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(reminderTime,  ParsePosition(0))
    if (date != null) {
        val reminder = Reminder(
            reminderId = reminderId,
            message = message,
            locationX = reminderLon,
            locationY = reminderLat,
            reminderTime = date.time,
            creationTime = creationTime,
            creatorId = creatorId,
            sendNotification = notification,
            icon = icon
        )


        if (notification) {
            val difference: Long = (notificationTime - Date().time) / 1000

            reminderViewModel.cancelReminderNotification(
                creationTime = creationTime,
                delay = difference
            )
            val updateReminder = Reminder(
                reminderId = reminder.reminderId,
                message = reminder.message,
                locationX = reminder.locationX,
                locationY = reminder.locationY,
                reminderTime = reminder.reminderTime,
                creationTime = Date().time,
                creatorId = reminder.creatorId,
                sendNotification = reminder.sendNotification,
                icon = reminder.icon
            )

            val differenceToNew: Long = (reminder.reminderTime - Date().time) / 1000
            if (reminder.sendNotification && (difference > 0)) {
                reminderViewModel.setReminderNotification(
                    delay = differenceToNew,
                    reminder = updateReminder,
                    daily = false,
                    weekly = false
                )
            }
        }

        reminderViewModel.updateReminder(
            reminder = reminder
        )
    }
    navController.navigate(route = "home")
}

fun deleteReminder(
    reminderViewModel: ReminderViewModel,
    navController: NavController,
    reminderId: Long,
    creationTime: Long,
    notificationTime: Long
) {

    val difference: Long = (notificationTime - Date().time) / 1000


    //canceling a notification
    reminderViewModel.cancelReminderNotification(
        creationTime = creationTime,
        delay = difference
    )

    reminderViewModel.deleteReminder(
        reminder = Reminder(
            reminderId = reminderId,
            message = "delete",
            locationX = 0.toDouble(),
            locationY = 0.toDouble(),
            reminderTime = notificationTime,
            creationTime = creationTime,
            creatorId = 1,
            sendNotification = false,
            icon = "Default"
        )
    )
    navController.navigate(route = "home")
}


private fun Long.formatToString(): String {
    return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(this)
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