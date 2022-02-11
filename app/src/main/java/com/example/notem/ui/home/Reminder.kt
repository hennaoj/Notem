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
import com.example.notem.data.user.UserViewModelFactory
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
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    val users = userViewModel.readAllData.observeAsState(listOf()).value
    var userId: Long = 1

    for (i in users.indices) {
        if (users[i].loggedIn) {
            userId = users[i].userId
        }
    }

    val list: MutableList<Reminder> = mutableListOf()

    for (item in reminders) {
        if (item.creatorId == userId) {
            list.add(item)
        }
    }

    Column(
        modifier = modifier
    ) {
        ReminderList(
            list = list,
            navController = navController
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>,
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
    val icon = ConvertToImageVector(icon = reminder.icon)
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
            text = reminder.message,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            color = Color.Black,
            modifier = Modifier.constrainAs(reminderConstrain) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 24.dp,
                    endMargin = 24.dp,
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

private fun ConvertToImageVector(icon: String): ImageVector {
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
            Icons.Filled.School
        }
        else -> Icons.Filled.StickyNote2
    }
}