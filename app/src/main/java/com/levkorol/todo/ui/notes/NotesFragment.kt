package com.levkorol.todo.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.folder.AddFolderFragment
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.ui.notes.NotesFragment.NotesFilter.Companion.IMPORTANT_NOTES
import com.levkorol.todo.ui.notes.NotesFragment.NotesFilter.Companion.NOTES_IN_SCHEDULE
import com.levkorol.todo.ui.notes.NotesFragment.NotesFilter.Companion.ONLY_FOLDER
import com.levkorol.todo.ui.notes.NotesFragment.NotesFilter.Companion.ONLY_NOTES
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment() {
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: Adapter
    private var folderId = -1L
    private var parentFolderId: Long = -1
    private var childElements: MutableList<Base>? = null
    private var query: String = ""
    private var notesFilter = -1

    companion object {
        private const val PARENT_FOLDER = "ParentId"
        fun newInstance(parentFolderId: Long): NotesFragment {
            val fragment = NotesFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(PARENT_FOLDER, parentFolderId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            parentFolderId = arguments?.getLong(PARENT_FOLDER, 0)!!
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = Adapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter


        add_nf.setOnClickListener {
            showAlterDialog()
        }

        add_notes_or_folder.setOnClickListener {
            showAlterDialog()
        }

        filter_btn.setOnClickListener {
            filterDialog()
        }

        clear_filter.setOnClickListener {
            notesFilter = -1
            updateNotes()
        }

        searchView.queryHint = "Поиск"
        search_frame.setOnClickListener {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    this@NotesFragment.query = query ?: ""
                    updateNotes()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    query = newText ?: ""
                    updateNotes()
                    return true
                }
            })
            searchView.isIconified = false
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        observeNotes()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(NotesFragment())
    }

    private fun observeNotes() {
        viewModel.getFolders().observe(this, Observer { folders ->
            val currentFolder = folders.firstOrNull { it.id == folderId }

            childElements = mutableListOf()
            if (currentFolder != null) {
                childElements!!.addAll(currentFolder.folders.sortedByDescending { it.date })
                childElements!!.addAll(currentFolder.notes.sortedByDescending { it.date })
            }
            updateNotes()

            if (childElements == null || childElements!!.size <= 0 || adapter.data.isEmpty()) {
                dont_have_notes.visibility = View.VISIBLE
            } else {
                dont_have_notes.visibility = View.GONE
            }
        })
    }

    private fun updateNotes() {
        if (notesFilter >= 0) {
            clear_filter.visibility = View.VISIBLE
        } else {
            clear_filter.visibility = View.GONE
        }
        adapter.data = childElements!!
            .filter { element ->
                if (notesFilter < 0) {
                    when (element) {
                        is Folder -> element.nameFolder.toLowerCase().trim().indexOf(query.toLowerCase().trim()) != -1
                        is Note -> element.name.toLowerCase().trim().indexOf(query.toLowerCase().trim()) != -1
                        else -> false
                    }
                } else {
                    when {
                        element is Folder && notesFilter == ONLY_FOLDER -> true
                        element is Note && notesFilter == ONLY_NOTES -> true
                        element is Note && notesFilter == IMPORTANT_NOTES && element.star -> true
                        element is Note && notesFilter == NOTES_IN_SCHEDULE && element.addSchedule -> true
                        else -> false
                    }
                }
            }
            .sortedByDescending { it.date }
        //  .map { text -> text.toLowerCase().trim() }


        if (notesFilter == NotesFilter.OLD_FOLDER_AND_NOTES) adapter.data =
            childElements!!.sortedBy { it.date }
        adapter.notifyDataSetChanged()
    }

    private fun filterDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Показать:")
        val pictureDialogItems =
            arrayOf(
                "Только папки",
                "Только заметки",
                "Важные заметки",
                "Старые папки и заметки",
                "Заметки с датой и временем"
                //  "Заметки с включенным оповещением"
            )
        builder.setItems(
            pictureDialogItems
        ) { _, selectionNumber ->
            notesFilter = selectionNumber
            updateNotes()
        }
        builder.show()
    }

    private class NotesFilter {
        companion object {
            const val ONLY_FOLDER: Int = 0
            val ONLY_NOTES = 1
            val IMPORTANT_NOTES = 2
            val OLD_FOLDER_AND_NOTES = 3
            val NOTES_IN_SCHEDULE = 4
        }
    }

    private fun showAlterDialog() {
        val builder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
        builder.setMessage("Что вы хотите создать?")
        builder.setPositiveButton("Заметку") { _, _ ->
            (activity as MainActivity).loadFragment(AddNoteFragment.newInstance(parentFolderId))
        }
        builder.setNegativeButton("Папку") { _, _ ->
            (activity as MainActivity).loadFragment(AddFolderFragment.newInstance(parentFolderId))
        }
        builder.setNeutralButton("Отменить") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}