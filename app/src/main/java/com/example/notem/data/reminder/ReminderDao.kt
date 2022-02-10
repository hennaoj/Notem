package com.example.notem.data.reminder

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notem.data.reminder.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    fun getAll(): LiveData<List<Reminder>>

    @Query("SELECT * from reminders where reminderId = :id")
    fun getById(id: Int): Reminder?

    @Insert
    suspend fun insert(item: Reminder)

    @Update
    suspend fun update(item: Reminder)

    @Delete
    suspend fun delete(item: Reminder)

}