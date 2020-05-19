package com.levkorol.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var title: String,
    var description: String,
    var date: Long,
    var checkBoxDone: Boolean,
    var alarm: Boolean
)