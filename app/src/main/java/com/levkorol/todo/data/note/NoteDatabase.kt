package com.levkorol.todo.data.note

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule

@Database(entities = [Note::class, Folder::class, Schedule::class], version = 1)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
    abstract fun scheduleDao(): ScheduleDao
}
