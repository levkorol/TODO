package com.levkorol.todo

import android.app.Application
import androidx.room.Room
import com.levkorol.todo.data.note.NoteDatabase
import com.levkorol.todo.data.note.NoteRepository


import com.totality.IScope

class NotesApplication : Application() {

    companion object{
        var applicationScope: IScope? = null
            private set
    }

    private lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()
        NoteRepository.initialize(database)
    }

}
