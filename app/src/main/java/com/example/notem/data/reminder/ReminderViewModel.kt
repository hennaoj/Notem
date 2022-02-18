package com.example.notem.data.reminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.notem.Graph
import com.example.notem.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.example.notem.R

class ReminderViewModel(application: Application) : ViewModel() {

    val readAllData: LiveData<List<Reminder>>
    private val repository: ReminderRepository

    init {
        val reminderDao = ReminderDatabase.getInstance(application).reminderDao()
        repository = ReminderRepository(reminderDao)
        readAllData = repository.readAllData
        createNotificationChannel(context = Graph.appContext)
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReminder(reminder = reminder)
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(reminder = reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder = reminder)
        }
    }

    fun setReminderNotification(delay: Long, reminder: Reminder, daily: Boolean, weekly: Boolean) {
        val workManager = WorkManager.getInstance(Graph.appContext)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS) //setting the delay
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        //Monitoring for state of work
        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    var sendNotification = true

                    if (sendNotification) {
                        createReminderNotification(reminder = reminder)
                    }
                    //setting another reminder if daily or weekly repeat is on
                    if (daily) {
                        setReminderNotification(delay = 86400, reminder = reminder, daily = daily, weekly = weekly)
                    }
                    if (weekly) {
                        setReminderNotification(delay = 604800, reminder = reminder, daily = daily, weekly = weekly)
                    }
                }
            }
    }
}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
            enableLights(true)
            lightColor = Color.BLUE
        }
        // register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}

private fun createReminderNotification(reminder: Reminder) {
    //using the reminder's creation time to get a unique id
    val id = reminder.creationTime.toInt()
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Notem Reminder")
        .setContentText(reminder.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setLights(Color.BLUE, 5000, 5000)


    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(id, builder.build())
    }
}