package com.levkorol.todo

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.data.note.NoteDatabase


class NotesApplication : Application() {

    private lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        //database = Room.databaseBuilder(this, NoteDatabase::class.java, "notes").build()
        //  AndroidThreeTen.init(this)
        ctx = this

        database = Room.databaseBuilder(this, NoteDatabase::class.java, "todo")
            .allowMainThreadQueries()
            // addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            //.addMigrations(MIGRATION_1_2)
            .build()
        MainRepository.initialize(database)

        Stetho.initializeWithDefaults(this)
    }

    companion object {

        lateinit var ctx: Context
    }
}
