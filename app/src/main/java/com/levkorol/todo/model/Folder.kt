package com.levkorol.todo.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var nameFolder: String,
    var descriptionFolder: String,
    var color: Int,
    var parentFolderId: Long = -1,
    override var date: Long
) : Base() {
    @Ignore
    var folders: List<Folder> = listOf<Folder>()
    @Ignore
    var notes: List<Note> = listOf<Note>()

}