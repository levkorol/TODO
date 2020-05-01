package com.levkorol.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "star") var star: Boolean,
    @ColumnInfo(name = "photo") var photo: String,
    @ColumnInfo(name = "date") override var date: Long,
    var parentFolderId: Long = -1
) : Base()




















