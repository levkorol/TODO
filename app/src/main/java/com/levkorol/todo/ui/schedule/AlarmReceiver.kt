package com.levkorol.todo.ui.schedule

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
            intent!!.getIntExtra("notificationId", 0),
            Notification.Builder(context).apply {
                setSmallIcon(R.drawable.ic_star)
                setContentTitle(intent.getStringExtra("title"))
                setContentText(intent.getCharSequenceExtra("text"))
                setWhen(System.currentTimeMillis())
                setTicker(intent.getCharSequenceExtra("ticker"))
                setPriority(Notification.PRIORITY_DEFAULT)
                setAutoCancel(true)
                setDefaults(Notification.DEFAULT_SOUND)
                setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0))
            }.build()
        )
    }
}