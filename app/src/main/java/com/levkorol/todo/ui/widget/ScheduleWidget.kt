package com.levkorol.todo.ui.widget

import android.appwidget.AppWidgetManager

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.levkorol.todo.R
import java.util.*

class ScheduleWidget : AppWidgetProvider() {

    private val LOG_TAG = "myLogs"

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
}
