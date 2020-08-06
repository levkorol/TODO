package com.levkorol.todo.data.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Folder.Background.PURPLE
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.model.Targets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object MainRepository {

    private lateinit var noteDao: NoteDao
    private lateinit var folderDao: FolderDao
    private lateinit var scheduleDao: ScheduleDao
    private lateinit var targetsDao: TargetDao

    private var resultFolders: MutableLiveData<List<Folder>> = MutableLiveData()
    private var schedules: MutableLiveData<List<Schedule>> = MutableLiveData()
    private var targetsML: MutableLiveData<List<Targets>> = MutableLiveData()
    var notes: List<Note>? = null
    var folders: List<Folder>? = null
    var schedule: List<Schedule>? = null
    var targets: List<Targets>? = null


    fun initialize(database: NoteDatabase) {
        noteDao = database.noteDao()
        folderDao = database.folderDao()
        scheduleDao = database.scheduleDao()
        targetsDao = database.targetsDao()

        targetsDao.getAll().observeForever { target ->
            this.targets = target
            targetsML.value = target
        }

        scheduleDao.getAll().observeForever { schedule ->
            this.schedule = schedule
            schedules.value = schedule
        }

        noteDao.getAll().observeForever { notes ->
            this.notes = notes
            process()
        }

        folderDao.getAll().observeForever { folders ->
            this.folders = folders
            process()
        }
    }

    fun getTargets(): LiveData<List<Targets>> = targetsDao.getAll()
    fun addTargets(targets: Targets) {
        GlobalScope.launch(Dispatchers.IO) {
            targetsDao.insert(targets)
        }
    }
    fun deleteTarget(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            targetsDao.deleteById(id)
        }
    }
    fun updateTarget(targets: Targets) {
        GlobalScope.launch(Dispatchers.IO) {
            targetsDao.update(targets)
        }
    }
    fun getAllTargetsNow(): List<Targets> = targetsDao.getAllTargetsNow()



    //Schedule
    fun getSchedules(): LiveData<List<Schedule>> = scheduleDao.getAll()

    fun getSchedule(): LiveData<Schedule> = scheduleDao.getId(-1)
    fun addSchedule(schedule: Schedule, code: (Long) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = scheduleDao.insert(schedule)
            code.invoke(id)
        }
    }

    fun deleteSchedule(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            scheduleDao.deleteById(id)
        }
    }

    fun updateSchedule(schedule: Schedule) {
        GlobalScope.launch(Dispatchers.IO) {
            scheduleDao.update(schedule)
        }
    }

    fun getAllSchedulesNow(): List<Schedule> = scheduleDao.getAllScheduleNow()


    //Notes
    @Deprecated("use getNotes() instead")
    fun getDeprecatedNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getNotes(): LiveData<List<Note>> = noteDao.getAll()
    fun getNotesNow(): List<Note> = noteDao.getAllNow()
    fun getNote(): LiveData<Note> = noteDao.getNoteId(-1)
    fun addNote(note: Note, code: (Long) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = noteDao.insert(note)
            code.invoke(id)
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

    // Folders
    fun getAllFoldersNow(): List<Folder> = folderDao.getAllFoldersNow()

    fun getResultFolders(): LiveData<List<Folder>> = resultFolders
    fun getFolder(): LiveData<Folder> = folderDao.getFolderId(-1)
    fun addFolder(folder: Folder) {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.insert(folder)
        }
    }

    fun deleteFolderById(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.deleteById(id)
        }
    }

    fun deleteAllNotes() {
        GlobalScope.launch(Dispatchers.IO) {
            noteDao.deleteAll()
        }
    }

    fun deleteAllFolders() {
        GlobalScope.launch(Dispatchers.IO) {
            folderDao.deleteAll()
        }
    }

    fun deleteAllSchedules() {
        GlobalScope.launch(Dispatchers.IO) {
            scheduleDao.deleteAll()
        }
    }

    private fun process() {
        if (folders == null || notes == null) {
            return
        }
        val rootFolder = Folder(
            -1, "Kornevaya papka"
            , Folder.Background.PINK,
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

