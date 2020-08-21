package com.levkorol.todo.data.note

import androidx.room.TypeConverter
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Targets

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

    @TypeConverter
    fun toBackgroundTarget(value: Int) : Targets.BackgroundTarget {
        return Targets.BackgroundTarget.values().first { background ->
            background.value == value }
    }

    @TypeConverter
    fun fromBackgroundTarget(background: Targets.BackgroundTarget) :Int {
        return background.value
    }

}