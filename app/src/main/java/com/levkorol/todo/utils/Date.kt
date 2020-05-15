package com.levkorol.todo.utils

import com.levkorol.todo.model.Base
import java.text.SimpleDateFormat
import java.util.*

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
    fun dateToString(date: Base): String {
        val dateFormat = SimpleDateFormat("EEEE, dd MMM, yyyy , hh:mm")
        return dateFormat.format(date)
    }
}