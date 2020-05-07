package com.levkorol.todo.data.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object NoteRepository {

    private lateinit var noteDao: NoteDao
    private lateinit var folderDao: FolderDao

    private var resultFolders: MutableLiveData<List<Folder>> = MutableLiveData()
     var notes: List<Note>? = null
     var folders: List<Folder>? = null


    fun initialize(database: NoteDatabase) {
        noteDao = database.noteDao()
        folderDao = database.folderDao()

        noteDao.getAll().observeForever { notes ->
            this.notes = notes
            process()
        }

        folderDao.getAll().observeForever { folders ->
            this.folders = folders
            process()
        }
    }

    @Deprecated("use getNotes() instead")
    fun getDeprecatedNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getFolders(): LiveData<List<Folder>> = resultFolders

    fun getNote(): LiveData<Note> = noteDao.getNoteId(-1)

    fun getFolder(): LiveData<Folder> = folderDao.getFolderId(-1)

    fun addNote(note: Note) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    fun addFolder(folder: Folder) {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.insert(folder)
        }
    }


    fun deleteById(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.deleteById(id)
        }
    }

    fun deleteFolderById(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.deleteById(id)
        }
    }

    fun update(note: Note) {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    fun update(folder: Folder) {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.update(folder)
        }
    }

    private fun process() {
        if (folders == null || notes == null) {
            return
        }
        val rootFolder = Folder(
            -1, "Kornevaya papka",
            "Kornevaya papka opisaniye", 0,
            0, 0
        )
        rootFolder.notes = notes!!.filter { it.parentFolderId == -1L }
        rootFolder.folders = folders!!.filter { it.parentFolderId == -1L }
        for (element in folders!!) {
            val childNotes = notes!!.filter { it.parentFolderId == element.id }
            element.notes = childNotes
            val childFolders = folders!!.filter { it.parentFolderId == element.id }
            element.folders = childFolders
        }
        folders = folders!!.union(listOf(rootFolder)).toList()
        resultFolders.value = folders
    }
}

