package com.levkorol.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var description: String,
    var date: Long,
    var checkBoxDone: Boolean,
    var time: Long,
    var alarm: Boolean,
    var addTime: Boolean,
    var dateCreate: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "", 0, false, 0, false, false)
}