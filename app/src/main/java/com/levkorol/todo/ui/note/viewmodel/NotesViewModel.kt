package com.levkorol.todo.ui.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note


class NotesViewModel : ViewModel() {

    fun getDeprecatedNotes(): LiveData<List<Note>> = MainRepository.getDeprecatedNotes()

    fun getFolders(): LiveData<List<Folder>> = MainRepository.getResultFolders()

    fun getNote(): LiveData<Note> = MainRepository.getNote()

    fun getNotes(): LiveData<List<Note>> = MainRepository.getNotes()
}
//class NotesViewModel : ViewModel() {
//
//    private val mainRepo = MainRepository
//
//    private val childElements = mutableListOf<Base>()
//
//    private var notesFilter: NotesFilter = NotesFilter.NO_FILTER
//    private var folderId = 0L
//
//    var query: String = ""
//
//    val isClearFilterVisible = MutableLiveData(false)
//    val isDontHaveNotesVisible = MutableLiveData(false)
//
//    lateinit var helper: NotesFragment.Helper
//    lateinit var adapter: NotesAndFoldersAdapter
//    lateinit var lifecycleOwner: LifecycleOwner
//    var parentFolderId: Long = 0L
//
//    fun init() {
//        mainRepo.getResultFolders().observeForever(observer)
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        mainRepo.getResultFolders().removeObserver(observer)
//    }
//
//    fun getDeprecatedNotes(): LiveData<List<Note>> = MainRepository.getDeprecatedNotes()
//
//    fun getNotes(): LiveData<List<Note>> = MainRepository.getNotes()
//
//
//    fun onAddClicked() {
//        val builder = helper.createAlertDialogBuilder()
//        builder.setMessage("Что вы хотите создать?")
//        builder.setPositiveButton("Заметку") { _, _ ->
//            helper.showAddNoteFragment(parentFolderId)
//        }
//        builder.setNegativeButton("Папку") { _, _ ->
//            helper.showAddFolderFragment(parentFolderId)
//        }
//        builder.setNeutralButton("Отменить") { _, _ ->
//        }
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }
//
//    fun onFilterClicked() {
//        val builder = helper.createAlertDialogBuilder()
//        // TODO все строки в ресурсы
//        builder.setTitle("Показать:")
//        val pictureDialogItems =
//            arrayOf(
//                "Только папки",
//                "Только заметки",
//                "Важные заметки",
//                "Старые папки и заметки",
//                "Заметки с датой и временем"
//            )
//
//        builder.setItems(pictureDialogItems) { _, selectionNumber ->
//            notesFilter = NotesFilter.values()[selectionNumber]
//            updateNotes()
//        }
//        builder.show()
//    }
//
//    fun onClearFilterClicked() {
//        notesFilter = NotesFilter.NO_FILTER
//        updateNotes()
//    }
//
//    fun updateNotes() {
//        isClearFilterVisible.value = notesFilter != NotesFilter.NO_FILTER
//
//        adapter.data = childElements
//            .filter { element ->
//                if (notesFilter == NotesFilter.NO_FILTER) {
//                    when (element) {
//                        is Folder -> element.nameFolder.toLowerCase().trim().indexOf(query.toLowerCase().trim()) != -1
//                        is Note -> element.name.toLowerCase().trim().indexOf(query.toLowerCase().trim()) != -1
//                        else -> false
//                    }
//                } else {
//                    when {
//                        element is Folder && notesFilter == NotesFilter.ONLY_FOLDER -> true
//                        element is Note && notesFilter == NotesFilter.ONLY_NOTES -> true
//                        element is Note && notesFilter == NotesFilter.IMPORTANT_NOTES && element.star -> true
//                        element is Note && notesFilter == NotesFilter.NOTES_IN_SCHEDULE && element.addSchedule -> true
//                        else -> false
//                    }
//                }
//            }
//            .sortedByDescending { it.date }
////            .map { text -> text.toLowerCase().trim() }
//
//        if (notesFilter == NotesFilter.OLD_FOLDER_AND_NOTES) {
//            adapter.data = childElements.sortedBy { it.date }
//        }
//        adapter.notifyDataSetChanged()
//    }
//
//    private val observer = Observer<List<Folder>> { folders ->
//        val currentFolder = folders.firstOrNull { it.id == folderId }
//
//        childElements.clear()
//        if (currentFolder != null) {
//            childElements.addAll(currentFolder.folders.sortedByDescending { it.date })
//            childElements.addAll(currentFolder.notes.sortedByDescending { it.date })
//        }
//        updateNotes()
//
//        isDontHaveNotesVisible.value = childElements.isEmpty() || adapter.data.isEmpty()
//    }
//
//    private enum class NotesFilter {
//        ONLY_FOLDER,
//        ONLY_NOTES,
//        IMPORTANT_NOTES,
//        OLD_FOLDER_AND_NOTES,
//        NOTES_IN_SCHEDULE,
//        NO_FILTER;
//    }
//
//}
