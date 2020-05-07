package com.levkorol.todo.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note

class NotesViewModel : ViewModel() {

    fun getDeprecatedNotes(): LiveData<List<Note>> = NoteRepository.getDeprecatedNotes()

    fun getFolders(): LiveData<List<Folder>> = NoteRepository.getFolders()

    fun getNote(): LiveData<Note> = NoteRepository.getNote()

    fun getNotes(): LiveData<List<Note>> = NoteRepository.getNotes()
}