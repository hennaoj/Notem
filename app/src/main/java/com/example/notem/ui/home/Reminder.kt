package com.example.notem.ui.home

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.reminder.Reminder
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderListInit(
    modifier: Modifier = Modifier,
    reminders: List<Reminder>,
    navController: NavController
) {

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value
    var userId: Long = 1

    for (i in users.indices) {
        if (users[i].loggedIn) {
            userId = users[i].userId
        }
    }

    //boolean for choosing whether to show all reminders or the ones that have passed
    val showAll = remember { mutableStateOf(false) }
    val list: MutableList<Reminder> = mutableListOf()

    for (item in reminders) {
        // only showing reminders that the logged in user has made
        if (item.creatorId == userId) {
            //adding all if showAll set to true
            if (showAll.value) {
                list.add(item)
            }
            //if time difference between this moment and the reminder time is negative
            //the reminder has passed and is added to the list
            else if (item.reminderTime  - Date().time < 0) {
                list.add(item)
            }
        }
    }

    //NOTE! when using sortByDescending and when showAll is on the reminder messages do not show normally
    //and have weird alignment
    list.sortBy { it.reminderTime }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "show all",
                color = Color(0xFF000000)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Switch(
                checked = showAll.value,
                onCheckedChange = { showAll.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.offset(y = 0.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ReminderList(
            list = list,
            navController = navController
        )
    }
}

@Composable
private fun ReminderList(
    list: MutableList<Reminder>,
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                reminder = item,
                modifier = Modifier.fillParentMaxWidth(),
                navController = navController
            )
        }

    }
    Divider(
        color = MaterialTheme.colors.primary,
        thickness = 7.dp
    )
}

@Composable
fun ReminderListItem(
    reminder: Reminder,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val icon = convertToImageVector(icon = reminder.icon)
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (divider, reminderConstrain, box, date, edit, iconCons) = createRefs()
        Divider(
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .constrainAs(box) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = when {
                reminder.reminderTime != null -> { reminder.reminderTime.formatToString() }
                else -> Date().formatToString()
            },
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = box.start,
                    end = box.end,
                    startMargin = 24.dp,
                    endMargin = 24.dp,
                    bias = 0f
                )
                centerVerticallyTo(reminderConstrain)
                bottom.linkTo(reminderConstrain.top)
                top.linkTo(parent.top)
            }
        )
        Text(
            text = reminder.message.replace("\n",""),
            style = MaterialTheme.typography.body1,
            maxLines = 10,
            color = Color.Black,
            modifier = Modifier.constrainAs(reminderConstrain) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 24.dp,
                    endMargin = 5.dp,
                    bias = 0f
                )
                top.linkTo(
                    date.bottom,
                    margin = 5.dp
                )
                bottom.linkTo(
                    parent.bottom,
                    margin = 20.dp
                )
                width = Dimension.preferredWrapContent
            }
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.constrainAs(iconCons) {
                linkTo(
                    start = date.end,
                    end = parent.end,
                    startMargin = 10.dp,
                    endMargin = 10.dp,
                    bias = 0f
                )
                top.linkTo(
                    box.top,
                    margin = 8.dp
                )
            },
            tint = Color.Black
        )
        ClickableText(
            //adding the reminder id to the route to get access to it in the edit reminder screen
            onClick = { navController.navigate(route = "editReminder/".plus(reminder.reminderId)) },
            modifier = Modifier.constrainAs(edit) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 20.dp,
                    endMargin = 10.dp,
                    bias = 1f
                )
                top.linkTo(
                    box.top,
                    margin = 5.dp
                )
            },
            text = AnnotatedString(text = "..."),
            style = MaterialTheme.typography.h6
        )

    }
}

private fun Long.formatToString(): String {
    return SimpleDateFormat("EEE, d MMM yyyy, 'klo' HH:mm", Locale.getDefault()).format(this)
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("EEE, d MMM yyyy, 'klo' HH:mm", Locale.getDefault()).format(this)
}

private fun convertToImageVector(icon: String): ImageVector {
    return when (icon) {
        "Default" -> {
            Icons.Filled.StickyNote2
        }
        "Work" -> {
            Icons.Filled.Work
        }
        "Medical" -> {
            Icons.Filled.MedicalServices
        }
        "Finances" -> {
            Icons.Filled.Paid
        }
        "School" -> {
            Icons.Filled.School
        }
        "Event" -> {
            Icons.Filled.Event
        }
        else -> Icons.Filled.StickyNote2
    }
}