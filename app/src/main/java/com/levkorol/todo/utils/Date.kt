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




enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

object Tools {
    fun dateToString(date: Long): String {
        val dateFormat = SimpleDateFormat("EEEE, dd MMM, yyyy ")
        return dateFormat.format(date)
    }

    fun convertLongToTimeString(systemTime: Long): String {
        return SimpleDateFormat("HH:mm")
            .format(systemTime).toString()
    }
}

fun isToday(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    val thenDay = calendar.get(DAY_OF_MONTH)
    calendar.timeInMillis = currentTimeMillis()
    return thenYear == calendar.get(YEAR)
            && thenMonth == calendar.get(MONTH)
            && thenDay == calendar.get(DAY_OF_MONTH)
}

fun isSameWeek(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    val thenWeek = calendar.get(WEEK_OF_MONTH)
    val thenDay = calendar.get(DAY_OF_MONTH)
    calendar.timeInMillis = currentTimeMillis()

    return thenYear == calendar.get(YEAR)
            && thenMonth == calendar.get(MONTH)
            && thenWeek == calendar.get(WEEK_OF_MONTH)
            && thenDay != calendar.get(DAY_OF_MONTH)
}

fun isMounth(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    val thenWeek = calendar.get(WEEK_OF_MONTH)
    val thenDay = calendar.get(DAY_OF_MONTH)
    calendar.timeInMillis = currentTimeMillis()
    return thenYear == calendar.get(YEAR)
            && thenMonth == calendar.get(MONTH)
            && thenDay != calendar.get(DAY_OF_MONTH)
            && thenWeek != calendar.get(WEEK_OF_MONTH)
}