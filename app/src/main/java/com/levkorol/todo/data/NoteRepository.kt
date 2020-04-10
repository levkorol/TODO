package com.levkorol.todo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levkorol.todo.model.Note

object NoteRepository {

    private val notes: MutableList<Note> = mutableListOf()
    private val notesLiveData: MutableLiveData<List<Note>> by lazy { MutableLiveData<List<Note>>() }

    fun getNotes(): LiveData<List<Note>> = notesLiveData

    fun addNote(note: Note) {
        // TODO
        notesLiveData.value = notes
    }

    fun removeNote(noteId: Long) {
        // TODO
        notesLiveData.value = notes
    }

    fun updateNote(note: Note) {
        // TODO
        notesLiveData.value = notes
    }

}