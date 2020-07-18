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
        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy ", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun dateToStringtwo(date: Long): String {
        val dateFormat = SimpleDateFormat(" dd MMM", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun convertLongToTimeString(systemTime: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(systemTime).toString()
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
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


fun areDatesEqual(date1: Long, date2: Long): Boolean {
    val calendar1 = GregorianCalendar()
    calendar1.timeInMillis = date1
    val calendar2 = GregorianCalendar()
    calendar2.timeInMillis = date2
    return calendar1.get(YEAR) == calendar2.get(YEAR)
            && calendar1.get(MONTH) == calendar2.get(MONTH)
            && calendar1.get(DAY_OF_MONTH) == calendar2.get(DAY_OF_MONTH)
}

fun getMillisecondsWithoutCurrentTime(milliseconds: Long): Long {
    val calendarWithCurrentTime = GregorianCalendar()
    calendarWithCurrentTime.timeInMillis = milliseconds
    val calendarWithoutCurrentTime = GregorianCalendar()
    calendarWithoutCurrentTime.set(YEAR, calendarWithCurrentTime.get(YEAR))
    calendarWithoutCurrentTime.set(MONTH, calendarWithCurrentTime.get(MONTH))
    calendarWithoutCurrentTime.set(DAY_OF_MONTH, calendarWithCurrentTime.get(DAY_OF_MONTH))
    return calendarWithoutCurrentTime.timeInMillis
}

fun isSameWeek(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    calendar.firstDayOfWeek = MONDAY
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    val thenWeek = calendar.get(WEEK_OF_MONTH)
    calendar.timeInMillis = currentTimeMillis()

    return thenYear == calendar.get(YEAR)
            && thenMonth == calendar.get(MONTH)
            && thenWeek == calendar.get(WEEK_OF_YEAR)
}


fun isMounth(time: Long): Boolean {
    val calendar = GregorianCalendar()
    calendar.timeInMillis = time
    val thenYear = calendar.get(YEAR)
    val thenMonth = calendar.get(MONTH)
    calendar.timeInMillis = currentTimeMillis()
    return thenYear == calendar.get(YEAR)
            && thenMonth == calendar.get(MONTH)
}