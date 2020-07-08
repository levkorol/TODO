package com.levkorol.todo.ui.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
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
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        GlobalScope.launch(Dispatchers.IO) {
            //Log.v("TEST", "onReceive: $intent")
            val noteId = intent?.getLongExtra("ID", 0)!!
            // TODO помимо айдишнки нужно ещё получить isNote
            // TODO если isNote то getNotes() иначе getSchedules()
            val isNote = true
            val (title, description) = if (isNote) {
                val note = MainRepository.getNotesNow()?.firstOrNull { note -> note.id == noteId }
                Pair(note?.name, note?.description)
            } else {
                // TODO эта ветка для шедула
                val note = MainRepository.getNotes().value?.firstOrNull { note -> note.id == noteId }
                Pair(note?.name, note?.description)
            }

            createNotificationChannel(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager = NotificationManagerCompat.from(context)
            builder?.build()?.let { notificationManager.notify(NOTIFY_ID, it) }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = ""
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    //        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
//            intent!!.getIntExtra("notificationId", 0),
//            Notification.Builder(context).apply {
//                setSmallIcon(R.drawable.ic_star)
//                setContentTitle(intent.getStringExtra("title"))
//                setContentText(intent.getCharSequenceExtra("text"))
//                setWhen(System.currentTimeMillis())
//                setTicker(intent.getCharSequenceExtra("ticker"))
//                setPriority(Notification.PRIORITY_DEFAULT)
//                setAutoCancel(true)
//                setDefaults(Notification.DEFAULT_SOUND)
//                setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0))
//            }.build()
//        )
    companion object {
        private val NOTIFY_ID = 101
        private val CHANNEL_ID = "task channel"

        fun setAlarm(id: Long, isNote: Boolean, time: Long) {
            // TODO <-
        }
    }
}