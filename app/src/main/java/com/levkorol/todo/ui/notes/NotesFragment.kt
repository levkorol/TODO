package com.levkorol.todo.ui.notes

import android.os.Bundle
import android.util.Log
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
import com.levkorol.todo.R
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.folder.AddFolderFragment
import com.levkorol.todo.ui.note.AddNoteFragment
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment() {
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: Adapter
    private var folderId = -1L
    private var notes: List<Note>? = null
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

        // dont_have_notes.visibility = if (adapter.data.isEmpty())View.VISIBLE else View.GONE//todo   настроить висабилити
        // Log.d("size", "${adapter.data}")

        add_notes_or_folder.setOnClickListener {
            showAlterDialog()
        }

        filter_btn.setOnClickListener {
            filterDialog()
        }

        searchView.queryHint = "Поиск"
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
        })
    }

    private fun updateNotes() {
        adapter.data = childElements!!
            .filter { element ->
                var isFiltered = false
                if(notesFilter < 0) {
                    if (element is Folder) {
                        isFiltered = element.nameFolder.indexOf(query) != -1
                    } else if (element is Note) {
                        isFiltered = element.name.indexOf(query) != -1
                    }
                } else {
                    if (element is Folder && notesFilter == NotesFilter.ONLY_FOLDER) {
                        isFiltered = true
                    }
                    if (element is Note && notesFilter == NotesFilter.ONLY_NOTES) {
                        isFiltered = true
                    }
                }
                isFiltered

            }.sortedByDescending { it.date }
        adapter.notifyDataSetChanged()
    }

    private fun filterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Показать:")
        val pictureDialogItems =
            arrayOf(
                "Только папки", "Только заметки",
                "Важные заметки", "Старые папки и заметки"
            )
        builder.setItems(
            pictureDialogItems
        ) { a, selectionNumber ->
            notesFilter = selectionNumber
            updateNotes()
        }
        builder.show()
    }

    private class NotesFilter {
        companion object {
            val ONLY_FOLDER: Int  = 0
            val ONLY_NOTES = 1
            val IMPORTANT_NOTES = 2
            val OLD_FOLDER_AND_NOTES = 3
        }
    }

    private fun showAlterDialog() {
        val builder = AlertDialog.Builder(requireContext())
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