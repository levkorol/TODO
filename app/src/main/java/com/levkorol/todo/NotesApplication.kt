package com.levkorol.todo

import android.app.Application
import androidx.room.Room
import com.levkorol.todo.data.note.NoteDatabase
import com.levkorol.todo.data.note.MainRepository


class NotesApplication : Application() {

    private lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        //database = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()
        //  AndroidThreeTen.init(this)


        database = Room.databaseBuilder(this, NoteDatabase::class.java, "todo")
            // addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            //.addMigrations(MIGRATION_1_2)
            .build()
        MainRepository.initialize(database)
    }
}
