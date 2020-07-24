package com.levkorol.todo.ui.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        GlobalScope.launch(Dispatchers.IO) {
            val noteId = intent?.getLongExtra("ID", 0)!!
            val scheduleId = intent.getLongExtra("SCHEDULE_ID", 0)
            val isNote = intent.getBooleanExtra("NOTE", true)

            Log.d(
                "AlarmReceiver",
                "onReceiver = isNote = $isNote, scheduleId = $scheduleId, noteId = $noteId"
            )

            val (title, description) =
                if (isNote) {
                    val note =
                        MainRepository.getNotesNow().firstOrNull { note -> note.id == noteId }
                    Pair(note?.name, note?.description)
                } else {
                    val schedule = MainRepository.getAllSchedulesNow()
                        .firstOrNull { schedule -> schedule.id == scheduleId }
                    Pair("Напоминание:", schedule?.description)
                }

            val resultIntent = if (isNote) {
                intent.putExtra("IS_NOTE", true)
                intent.putExtra("ID", noteId)
                Intent(context, MainActivity::class.java)
            } else {
                intent.putExtra("IS_NOTE", false)
                Intent(context, MainActivity::class.java)
            }
            val int = Intent(context, MainActivity::class.java)
            createNotificationChannel(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context, 0, resultIntent, FLAG_UPDATE_CURRENT
                    )
                )
            val notificationManager = NotificationManagerCompat.from(context)
            builder?.build()?.let { notificationManager.notify(NOTIFY_ID, it) }
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

        fun setAlarm(id: Long, isNote: Boolean, time: Long) {
            //
        }
    }
}