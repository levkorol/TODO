package com.levkorol.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var description: String,
    var comment: String,
    var date: Long,
    var checkBoxDone: Boolean,
    var hours: Int,
    var minutes: Int,
    var alarm: Boolean,
    var addTime: Boolean,
    var archive: Boolean,
    var dateCreate: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "","", 0, false, 0,0, false, false,false)
}