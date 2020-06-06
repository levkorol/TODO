package com.levkorol.todo.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.levkorol.todo.R

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var nameFolder: String,
    var descriptionFolder: String,
    var background: Background,
    var parentFolderId: Long = -1,
    override var date: Long
) : Base() {
    @Ignore
    var folders: List<Folder> = listOf<Folder>()
    @Ignore
    var notes: List<Note> = listOf<Note>()

    enum class Background(
        val value: Int,
        @DrawableRes val res: Int
    ) {
        PURPLE(0, R.drawable.background_item_folder)
        // TODO добавить для отстальных цветов
    }
}