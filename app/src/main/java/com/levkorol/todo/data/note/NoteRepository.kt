package com.levkorol.todo.data.note

import android.util.Log
import androidx.lifecycle.LiveData
import com.levkorol.todo.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NoteRepository {

    private lateinit var noteDao: NoteDao

    fun initialize(database: NoteDatabase) {
        noteDao = database.noteDao()
    }

    fun getNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun addNote(note: Note) {
        noteDao.insert(note)
    }

    fun removeNote(noteId: Long) {

    }

    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }

    fun deleteById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.deleteById(id)
        }
    }

    fun update(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.update(note)
        }
    }
}