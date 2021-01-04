package com.levkorol.todo.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.widget.ScheduleWidgetProvider.Companion.ITEM_POSITION


data class WidgetItem(
    val id: Long,
    val description: String,
    val checkBox: Boolean,
    val timeHours: Int,
    val timeMinutes: Int
)

fun mapperForWidget(schedule: Schedule): WidgetItem {
    return WidgetItem(
        id = schedule.id,
        description = schedule.description,
        checkBox = schedule.checkBoxDone,
        timeHours = schedule.hours,
        timeMinutes = schedule.minutes
    )
}

class RemoteViewsFactory(
    private val context: Context,
    intent: Intent?
) : RemoteViewsService.RemoteViewsFactory {

    // тут будут данные для списка виджета
    private val testWidgetItems: MutableList<WidgetItem> = ArrayList()

    override fun onCreate() {
        // генерация тестовых данных
        for (i in 0 until 10) {
            testWidgetItems.add(WidgetItem(i.toLong(), "Задача №$i", true, 1, 1))
        }
    }

    override fun onDataSetChanged() {
        // empty
    }

    override fun onDestroy() {
        testWidgetItems.clear();
    }

    override fun getCount(): Int {
        return testWidgetItems.size
    }

    // здесь добавляем данные для айтемов виджета
    // TODO заменить на свои айтемы
    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_list_widget_schedule)
        rv.setTextViewText(R.id.tv_title_widget, testWidgetItems[position].description)
        // данные для клика
        val clickIntent = Intent()
        clickIntent.putExtra(ITEM_POSITION, position)
        rv.setOnClickFillInIntent(R.id.tv_title_widget, clickIntent)
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