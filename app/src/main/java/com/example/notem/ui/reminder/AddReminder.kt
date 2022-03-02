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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.reminder.Reminder
import com.example.notem.data.reminder.ReminderViewModel
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
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
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    //checking the current user by fetching the user who is logged in
    for (i in users.indices) {
        if (users[i].loggedIn) {
            userId = users[i].userId
        }
    }

    val reminderViewModel: ReminderViewModel = viewModel(
        factory =viewModelProviderFactoryOf { ReminderViewModel(context.applicationContext as Application) }
    )

    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value

    val longitude = remember { mutableStateOf(latlng?.longitude ?: 0)}
    val latitude = remember { mutableStateOf(latlng?.latitude ?: 0)}

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primaryVariant) {
        val message = rememberSaveable { mutableStateOf("") }
        val reminderTime = rememberSaveable { mutableStateOf("") }
        val icon = rememberSaveable { mutableStateOf("Default")}
        val checkedState = remember { mutableStateOf(true) }
        val dailyRepeat = remember { mutableStateOf(false) }
        val weeklyRepeat = remember { mutableStateOf(false) }

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
        }}
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
                label = { Text("dd-mm-yyyy hh:mm")}, //guide for date format
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = latitude.value.toString(),
                    onValueChange = {},
                    label = { Text("latitude")},
                    modifier = Modifier.fillMaxWidth(fraction = 0.33f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 1,
                    shape = MaterialTheme.shapes.small,
                    enabled = false
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = longitude.value.toString(),
                    onValueChange = {},
                    label = { Text("longitude")},
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 1,
                    shape = MaterialTheme.shapes.small,
                    enabled = false
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { navController.navigate("addLocation") },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.height(55.dp).padding(top = 15.dp)
                ) {
                    Text(text = "Open map")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            //switch for setting a notification
            Row {
                Text(
                    text = "send notification",
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    color = Color(0xFF000000)
                )
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.5f))
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                        if (weeklyRepeat.value) {
                            weeklyRepeat.value = !weeklyRepeat.value
                        }
                        if (dailyRepeat.value) {
                            dailyRepeat.value = !dailyRepeat.value
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //switch for setting the notification to repeat daily
            Row {
                Text(
                    text = "repeat daily",
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    color = Color(0xFF000000)
                )
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.5f))
                Switch(
                    checked = dailyRepeat.value,
                    onCheckedChange = {
                        dailyRepeat.value = it
                        if (weeklyRepeat.value) {
                            weeklyRepeat.value = !weeklyRepeat.value
                        }
                        if (!checkedState.value) {
                            checkedState.value = !checkedState.value
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //switch for setting the notification to repeat weekly
            Row {
                Text(
                    text = "repeat weekly",
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    color = Color(0xFF000000)
                )
                Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.5f))
                Switch(
                    checked = weeklyRepeat.value,
                    onCheckedChange = {
                        weeklyRepeat.value = it
                        if (dailyRepeat.value) {
                            dailyRepeat.value = !dailyRepeat.value
                        }
                        if (!checkedState.value) {
                            checkedState.value = !checkedState.value
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { addReminder(
                    message = message.value,
                    reminderTime = reminderTime.value,
                    reminderViewModel = reminderViewModel,
                    navController = navController,
                    userId = userId,
                    icon = icon.value,
                    notification = checkedState.value,
                    repeatDaily = dailyRepeat.value,
                    repeatWeekly = weeklyRepeat.value,
                    locationX = longitude.value.toDouble(),
                    locationY = latitude.value.toDouble()
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
    icon: String,
    notification: Boolean,
    repeatDaily: Boolean,
    repeatWeekly: Boolean,
    locationX: Double,
    locationY: Double
) {
    val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(reminderTime,  ParsePosition(0))

    //making sure that the date input was in the correct format before
    //creating a new reminder
    if (date != null) {
        val reminder = Reminder(
            message = message,
            locationX = locationX,
            locationY = locationY,
            reminderTime = date.time,
            creationTime = Date().time,
            creatorId = userId,
            sendNotification = notification,
            icon = icon
        )
        val difference: Long = (date.time - Date().time) / 1000

        reminderViewModel.addReminder(
            reminder = reminder
        )
        //if send notification was chosen, creating a new notification
        if (notification && (difference > 0)) {
            reminderViewModel.setReminderNotification(
                delay = difference,
                reminder = reminder,
                daily = repeatDaily,
                weekly = repeatWeekly
            )
        }
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


    //a list of icons and a list of strings that relate to those icons
    //the string is stored with the reminder to later fetch an icon in the home screen
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