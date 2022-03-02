package com.example.notem.data.reminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reminders")
data class Reminder(

    @PrimaryKey(autoGenerate = true)
    var reminderId: Long = 0L,

    @ColumnInfo(name = "message")
    var message: String,

    @ColumnInfo(name = "location_x")
    var locationX: Double,

    @ColumnInfo(name = "location_y")
    var locationY: Double,

    @ColumnInfo(name = "reminder_time")
    var reminderTime: Long,

    @ColumnInfo(name = "creation_time")
    var creationTime: Long,

    @ColumnInfo(name = "creator_id")
    var creatorId: Long,

    @ColumnInfo(name = "reminder_seen")
    var reminderSeen: Boolean = false,

    @ColumnInfo(name = "send_notification")
    var sendNotification: Boolean,

    @ColumnInfo(name = "icon")
    var icon: String

)

