package com.levkorol.todo.data.note

import androidx.room.TypeConverter
import com.levkorol.todo.model.Folder

class Converters {
    @TypeConverter
    fun fromBackground(value: Int) :Folder.Background {
        return Folder.Background.values().first { background ->
            background.value == value }
    }

    @TypeConverter
    fun toBackground(background: Folder.Background) :Int {
        return background.value
    }
}