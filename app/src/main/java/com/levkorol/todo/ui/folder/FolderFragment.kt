package com.levkorol.todo.ui.folder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.ui.notes.Adapter
import kotlinx.android.synthetic.main.folder_fragment.*
import kotlinx.android.synthetic.main.folder_fragment.back_profile


class FolderFragment : Fragment() {

    private lateinit var viewModel: FolderViewModel
    private lateinit var adapter: Adapter
    private var folderId = -1L
    private var folder: Folder? = null

    companion object {
        private const val FOLDER_ID = "FOLDER_ID"
        private const val TITLE = "TITLE"

        fun newInstance(folder: Folder) : FolderFragment {
            val fragment = FolderFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(FOLDER_ID, folder.id)
                putString(TITLE,folder.nameFolder)
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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_folder_fragment)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = Adapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

        title_folder.text = arguments?.getString(TITLE, "")

        add_note_or_folder.setOnClickListener {
            showAlterDialog()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
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
            }

            adapter.data = childElements.sortedByDescending { it.date }

            adapter.notifyDataSetChanged()

        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Что вы хотите создать?")
        builder.setPositiveButton("Заметку") { _, _ ->
            (activity as MainActivity).loadFragment(AddNoteFragment.newInstance(folderId))
        }
        builder.setNegativeButton("Папку") { _, _ ->
            (activity as MainActivity).loadFragment(AddFolderFragment.newInstance(folderId))
        }
        builder.setNeutralButton("Отменить") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
