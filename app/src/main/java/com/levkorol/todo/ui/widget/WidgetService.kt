package com.levkorol.todo.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return RemoteViewsFactory(this.applicationContext, intent)
    }
}