package com.levkorol.todo.data.note

import androidx.room.TypeConverter
import com.levkorol.todo.model.Folder

class Converters {
    @TypeConverter
    fun toBackground(value: Int) :Folder.Background {
        return Folder.Background.values().first { background ->
            background.value == value }
    }

    @TypeConverter
    fun fromBackground(background: Folder.Background) :Int {
        return background.value
    }

    // TODO
}