package com.example.notem.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.notem.data.entity.Reminder
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderListInit(
    modifier: Modifier = Modifier,
    reminders: List<Reminder>
) {
    Column(
        modifier = modifier
    ) {
        ReminderList(
            list = reminders
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                reminder = item,
                onClick = {},
                modifier = Modifier.fillParentMaxWidth()
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderConstrain, box, date) = createRefs()
        Divider(
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Box(
            modifier = Modifier.size(100.dp).constrainAs(box) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = when {
                reminder.reminderDate != null -> { reminder.reminderDate.formatToString() }
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
            text = reminder.reminderText,
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

    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("EEE, d MMM yyyy, 'klo' HH:mm", Locale.getDefault()).format(this)
}