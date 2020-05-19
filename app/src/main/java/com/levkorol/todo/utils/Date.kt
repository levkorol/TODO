package com.levkorol.todo.utils

import android.annotation.SuppressLint
import com.levkorol.todo.model.Base
import java.lang.System.currentTimeMillis
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

const val SECOND = 10000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}


@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

object Tools {
    fun dateToString(date: Base): String {
        val dateFormat = SimpleDateFormat("EEEE, dd MMM, yyyy , hh:mm")
        return dateFormat.format(date)
    }
}

fun isToday(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    val thenDay = calendar.get(DAY_OF_MONTH)
    calendar.timeInMillis = currentTimeMillis()
    return thenYear == calendar.get(YEAR) && thenMonth == calendar.get(MONTH) && thenDay == calendar.get(DAY_OF_MONTH)
}