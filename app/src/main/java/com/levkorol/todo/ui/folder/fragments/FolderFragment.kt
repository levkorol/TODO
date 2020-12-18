package com.levkorol.todo.ui.folder.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.base.BaseFragment
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.ui.folder.viewmodel.FolderViewModel
import com.levkorol.todo.ui.note.fragments.AddNoteFragment
import com.levkorol.todo.ui.note.recyclerview.NotesAndFoldersAdapter
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.folder_fragment.*
import kotlinx.android.synthetic.main.folder_name.view.*

class FolderFragment : BaseFragment() {

    private lateinit var viewModel: FolderViewModel
    private lateinit var adapter: NotesAndFoldersAdapter
    private var folderId = -1L
    private lateinit var folder: Folder
    private var title: String = ""

    companion object {
        private const val FOLDER_ID = "FOLDER_ID"
        private const val TITLE = "TITLE"

        fun newInstance(folder: Folder): FolderFragment {
            val fragment = FolderFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(FOLDER_ID, folder.id)
                putString(TITLE, folder.nameFolder)
            }

            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.folder_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderId = arguments?.getLong(FOLDER_ID, -1)!!
        title = arguments?.getString(TITLE, "").toString()


        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_folder_fragment)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = NotesAndFoldersAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

        title_folder.text = title

        add_note_or_folder.setOnClickListener {
            showAlterDialog()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        delete_folder.setOnClickListener {
            showAlterDelete()
        }

        edit_folder.setOnClickListener {
            dialog()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(FolderViewModel::class.java)
        observeNotes()
    }

    private fun observeNotes() {
        viewModel.getFolders().observe(this, Observer { folders ->
            val currentFolder = folders.firstOrNull { it.id == folderId }
            val childElements = mutableListOf<Base>()
            if (currentFolder != null) {
                childElements.addAll(currentFolder.folders.sortedByDescending { it.date })
                childElements.addAll(currentFolder.notes.sortedByDescending { it.date })
                folder = currentFolder
            }

            adapter.data = childElements.sortedByDescending { it.date }

            adapter.notifyDataSetChanged()

        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDialog() {
        val builder = MaterialAlertDialogBuilder(context!!)
        builder.setMessage("Что вы хотите создать?")
        builder.setPositiveButton("Заметку") { _, _ ->
            replaceFragment(AddNoteFragment.newInstance(folderId))
        }
        builder.setNegativeButton("Папку") { _, _ ->
            replaceFragment(AddFolderFragment.newInstance(folderId))
        }
        builder.setNeutralButton("Отменить") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDelete() {
        val builder = MaterialAlertDialogBuilder(context!!)
        builder.setMessage("Удалить папку $title?")
        builder.setPositiveButton("Да") { _, _ ->
            MainRepository.deleteFolderById(folderId)
            parentFragmentManager.popBackStack()
            // (activity as MainActivity).loadFragment(NotesFragment())
        }
        builder.setNegativeButton("Отмена") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun dialog() {
        val view = requireActivity().layoutInflater.inflate(R.layout.folder_name, null)
        val builder = MaterialAlertDialogBuilder(requireContext())
//        AlertDialog.Builder(requireContext())
        with(builder) {
         //   setBackground(ColorDrawable(resources.getColor(android.R.color.black)))
            setView(view)
            setPositiveButton("Сохранить") { _, _ ->
                folder.nameFolder = view.new_name.text.toString()
                MainRepository.updateFolder(folder)
                title_folder.text = folder.nameFolder
            }
            setNegativeButton("Отмена") { _, _ -> }
            setTitle("Изменить название папки")
            create()
            show()
        }
    }
}
