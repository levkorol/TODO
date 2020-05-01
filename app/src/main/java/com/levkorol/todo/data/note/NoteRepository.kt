package com.levkorol.todo.data.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NoteRepository {

    private lateinit var noteDao: NoteDao

    private var resultNotes: MutableLiveData<List<Base>> = MutableLiveData()
    private var notes: List<Note>? = null
    // TODO folders

    fun initialize(database: NoteDatabase) {
        noteDao = database.noteDao()
        // TODO folderDao

        noteDao.getAll().observeForever { notes ->
            this.notes = notes
            process()
        }
        // TODO так же как выше подписаться на getAll() у folders
    }

    @Deprecated("use getNotes() instead")
    fun getDeprecatedNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getNotes(): LiveData<List<Base>> = resultNotes;

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

    private fun process() {
        // TODO напишу текстом
    }

}