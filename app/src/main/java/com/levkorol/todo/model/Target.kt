package com.levkorol.todo.model

import androidx.room.PrimaryKey

data class Target(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var description: String,
    var date: Long,
    var targetDone: Boolean,
    var days: Int,
    var hours: Int,
    var minutes: Int,
    var dateCreate: Long = System.currentTimeMillis()
)

