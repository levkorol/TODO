package com.levkorol.todo.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.NoteRepository
import com.levkorol.todo.model.Note

class NotesViewModel : ViewModel() {

    fun getNotes(): LiveData<List<Note>> = NoteRepository.getNotes()

}