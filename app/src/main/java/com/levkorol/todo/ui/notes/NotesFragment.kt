package com.levkorol.todo.ui.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.NotesApplication
import com.levkorol.todo.data.IFragmentRepository
import com.levkorol.todo.R
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.ui.note.NoteFragment
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter
    private lateinit var note: List <Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = NotesAdapter()
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

//
//        recyclerView.addOnItemClickListener(object : OnItemClickListener {
//            override fun onItemClicked(position: Int, view: View) {
//                (activity as MainActivity).loadFragment(NoteFragment())
//
//            }
//        })

        add_notes_or_folder.setOnClickListener {
            showAlterDialog()
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
        viewModel.getNotes().observe(this, Observer<List<Note>> { notes ->
            adapter.data = notes
        })


    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Что вы хотите создать?")
        builder.setPositiveButton("Заметку") { _, _ ->
            (activity as MainActivity).loadFragment(AddNoteFragment())
        }
        builder.setNegativeButton("Папку") { _, _ ->

        }
        builder.setNeutralButton("Отменить") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
