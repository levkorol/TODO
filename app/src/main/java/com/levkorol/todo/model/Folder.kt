package com.levkorol.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var folderId: Long? = null,
    @ColumnInfo(name = "nameFolder") var nameFolder: String,
    @ColumnInfo(name = "descriptionFolder") var descriptionFolder: String,
    @ColumnInfo(name = "color") var color: Int
   )