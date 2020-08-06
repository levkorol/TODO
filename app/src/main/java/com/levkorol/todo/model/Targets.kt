package com.levkorol.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Targets(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    var description: String,
    var date: Long,
    var targetDone: Boolean,
    var days: Int,
    var time: Int,
    var dateCreate: Long = System.currentTimeMillis()
)

