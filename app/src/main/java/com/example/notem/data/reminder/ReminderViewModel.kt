package com.example.notem.data.reminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.notem.*
import com.example.notem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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

    fun cancelReminderNotification(creationTime: Long, delay: Long) {

        val workManager = WorkManager.getInstance(Graph.appContext)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        //the canceling should happen after the notification has been send
        //which is taken care of by the +0.03 in delay
        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay + 0.03.toLong(), TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        //Monitoring for state of work
        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val id = creationTime.toInt()
                    val context = Graph.appContext
                    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.cancel(id)
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

    val intent = Intent(Graph.appContext, MainActivity::class.java)
    val pendingIntent = TaskStackBuilder.create(Graph.appContext).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Notem Reminder")
        .setContentText(reminder.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)


    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(id, builder.build())
    }
}