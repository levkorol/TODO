package com.levkorol.todo.ui.folder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note

class FolderViewModel : ViewModel() {
    fun getDeprecatedNotes(): LiveData<List<Note>> = MainRepository.getDeprecatedNotes()

    fun getFolders(): LiveData<List<Folder>> = MainRepository.getResultFolders()

    fun getNote(): LiveData<Note> = MainRepository.getNote()

    fun getNotes(): LiveData<List<Note>> = MainRepository.getNotes()
}
