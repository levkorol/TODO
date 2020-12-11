package com.levkorol.todo.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.levkorol.todo.R


data class WidgetItem(val id: String, val name: String)

class RemoteViewsFactory(
    private val context: Context,
    intent: Intent?

) : RemoteViewsService.RemoteViewsFactory {

    private val mCount = 10
    private val mWidgetItems: MutableList<WidgetItem> = ArrayList<WidgetItem>()
    private val mContext: Context? = null
    private val mAppWidgetId = 0

    override fun onCreate() {
        for (i in 0 until mCount) {
            mWidgetItems.add(WidgetItem("$i!", "name - $i"))
        }
        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        // We sleep for 3 seconds here to show how the empty view appears in the interim.
        // The empty view is set in the StackWidgetProvider and should be a sibling of the
        // collection view.
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {
        mWidgetItems.clear();
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext!!.packageName, R.layout.item_list_widget_schedule)
        rv.setTextViewText(R.id.tv_title_widget, mWidgetItems[position].name)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}