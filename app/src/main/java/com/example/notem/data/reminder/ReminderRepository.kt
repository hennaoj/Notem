package com.example.notem.data.reminder

import androidx.lifecycle.LiveData
import com.example.notem.data.reminder.Reminder
import com.example.notem.data.reminder.ReminderDao

class ReminderRepository(private val reminderDao: ReminderDao) {

    val readAllData: LiveData<List<Reminder>> = reminderDao.getAll()

    suspend fun addReminder(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.update(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

}

