package com.levkorol.todo

import android.app.Application
import androidx.room.Room
import com.levkorol.todo.data.note.NoteDatabase
import com.levkorol.todo.data.note.NoteRepository


class NotesApplication : Application() {

    private lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()
        NoteRepository.initialize(database)
    }
}
