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
    var background: Background,
    var parentFolderId: Long = -1,
    override var date: Long
) : Base() {

    constructor() : this(0, "", Background.PINK, 0,0)

    @Ignore
    var folders: List<Folder> = listOf()
    @Ignore
    var notes: List<Note> = listOf()


    enum class Background(
        val value: Int,
        @DrawableRes val res: Int
    ) {
        PURPLE(0, R.drawable.background_item_folder),
        GRAY(1, R.drawable.bg_folder_gray),
        PINK(2, R.drawable.bg_folder_pink),
        GREEN(3, R.drawable.bg_folder_green)
    }
}