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

            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            val intent = Intent(context, WidgetService::class.java).apply {
                // Add the app widget ID to the intent extras.
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            // Instantiate the RemoteViews object for the app widget layout.
            val rv = RemoteViews(context?.packageName, R.layout.widget).apply {
                // Set up the RemoteViews object to use a RemoteViews adapter.
                // This adapter connects
                // to a RemoteViewsService  through the specified intent.
                // This is how you populate the data.
//                setRemoteAdapter(R.id.stack_view, intent)


                // The empty view is displayed when the collection has no items.
                // It should be in the same layout used to instantiate the RemoteViews
                // object above.
//                setEmptyView(R.id.stack_view, R.id.empty_view)
            }

            //
            // Do additional processing specific to this app widget...
            //

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
