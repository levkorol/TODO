package com.levkorol.todo

import android.app.Application
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.levkorol.todo.data.note.NoteDatabase
import com.levkorol.todo.data.note.MainRepository


class NotesApplication : Application() {

    private lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()
        MainRepository.initialize(database)
      //  AndroidThreeTen.init(this)
    }
}
