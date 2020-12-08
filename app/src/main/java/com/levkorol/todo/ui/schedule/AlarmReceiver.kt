package com.levkorol.todo.ui.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

         GlobalScope.launch(Dispatchers.IO) {

        val scheduleId = intent?.getLongExtra("SCHEDULE_ID", 0)

             val schedule = MainRepository.getAllSchedulesNow()
            .firstOrNull { schedule -> schedule.id == scheduleId }


        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_star)
            .setContentTitle("Напоминание")
            .setContentText(schedule?.description)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(schedule?.description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    context, 0,  Intent(context, MainActivity::class.java), FLAG_UPDATE_CURRENT
                )
            )
        val notificationManager = NotificationManagerCompat.from(context)

             builder.build().let { notificationManager.notify(NOTIFY_ID, it) }
          }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TODO"
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private val NOTIFY_ID = 101
        private val CHANNEL_ID = "task channel"
    }
}