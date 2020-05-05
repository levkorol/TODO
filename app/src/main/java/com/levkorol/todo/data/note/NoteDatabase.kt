package com.levkorol.todo.data.note

import androidx.room.Database
import androidx.room.RoomDatabase
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note

@Database(entities = [Note::class, Folder::class], version = 1)

abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
}
