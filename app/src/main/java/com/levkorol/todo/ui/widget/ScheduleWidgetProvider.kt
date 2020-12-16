package com.levkorol.todo.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.levkorol.todo.R
import java.util.*


class ScheduleWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        handleWidgetItemClick(intent, context)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d(LOG_TAG, "onEnabled")
    }

    override fun onUpdate(
        context: Context?, appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds))

        appWidgetIds?.forEach { appWidgetId ->
            val intent = Intent(context, WidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            val rv = RemoteViews(context?.packageName, R.layout.widget).apply {
                setRemoteAdapter(R.id.widgetListView, intent)
            }
            setWidgetListClick(rv, context, appWidgetId)

            appWidgetManager?.updateAppWidget(appWidgetId, rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)

    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds))
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(LOG_TAG, "onDisabled")
    }

    // обработка нажатия на элемент
    private fun handleWidgetItemClick(intent: Intent?, context: Context?) {
        intent?.let {
            if (intent.action.equals(ACTION_ON_CLICK, true)) {
                val itemPos = intent.getIntExtra(ITEM_POSITION, -1)
                if (itemPos != -1) {
                    Toast.makeText(
                        context, "Клик на элемент: $itemPos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setWidgetListClick(rv: RemoteViews, context: Context?, appWidgetId: Int) {
        val listClickIntent = Intent(context, ScheduleWidgetProvider::class.java)
        listClickIntent.action = ACTION_ON_CLICK
        val listClickPIntent = PendingIntent.getBroadcast(
            context, 0,
            listClickIntent, 0
        )
        rv.setPendingIntentTemplate(R.id.widgetListView, listClickPIntent)
    }

    companion object {
        const val ACTION_ON_CLICK = "action_on_click"
        const val ITEM_POSITION = "item_position"
        private const val LOG_TAG = "ScheduleWidgetProvider"
    }
}
