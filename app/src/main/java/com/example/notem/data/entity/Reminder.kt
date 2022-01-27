package com.example.notem.data.entity

import java.util.*

data class Reminder(
    val reminderId: Long,
    val reminderDate: Date?,
    val reminderText: String
)