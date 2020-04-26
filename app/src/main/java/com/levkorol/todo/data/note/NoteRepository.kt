package com.levkorol.todo.data.note

import android.util.Log
import androidx.lifecycle.LiveData
import com.levkorol.todo.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NoteRepository {

    private lateinit var noteDao: NoteDao

    fun initialize(database: NoteDatabase) {
        noteDao = database.noteDao()
    }

    fun getNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getNote(): LiveData<Note> = noteDao.getNoteId(-1)


    fun addNote(note: Note) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    fun delete(note: Note) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    fun deleteById(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.deleteById(id)
        }
    }

    fun update(note: Note) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.update(note)
        }
    }
}