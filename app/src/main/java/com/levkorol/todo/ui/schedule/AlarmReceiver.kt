package com.levkorol.todo.ui.schedule

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository.schedule
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesViewModel
import java.util.Observer

class AlarmReceiver : BroadcastReceiver() {
//    override fun getLifecycle(): Lifecycle {
//        observe()
//        return lifecycle
//    }

    private lateinit var viewModel: ScheduleViewModel
    private var schedules: Schedule? = null
    private var scheduleId : Long = 1

    override fun onReceive(context: Context, intent: Intent?) {
        Log.v("TEST", "onReceive: $intent")
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
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_star)
            .setContentTitle("Напоминание")
            .setContentText("${schedules?.description}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)
        builder?.build()?.let { notificationManager.notify(NOTIFY_ID, it) }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "asdasdassadasd"
            val descriptionText = "asdasdasdsad"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

//    private fun observe() {
//        viewModel.getSchedules().observe(this, androidx.lifecycle.Observer { schedule ->
//            schedules = schedule.firstOrNull{ s -> s.id == scheduleId}
//        })
//    }

    companion object {
        private val NOTIFY_ID = 101
        private val CHANNEL_ID = "task channel"
    }
}