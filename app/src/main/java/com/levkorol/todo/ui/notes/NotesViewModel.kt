package com.levkorol.todo.ui.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note

class NotesViewModel : ViewModel() {

    fun getNotes(): LiveData<List<Note>> = NoteRepository.getNotes()

    fun getNote(): LiveData<Note> = NoteRepository.getNote()
}