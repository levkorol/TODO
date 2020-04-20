package com.levkorol.todo.ui.note

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesAdapter
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.notes.NotesViewModel
import com.levkorol.todo.ui.schedule.ScheduleFragment
import com.levkorol.todo.ui.schedule.ScheduleViewModel
import kotlinx.android.synthetic.main.fragment_note.*

class NoteFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel

    companion object {

        private const val NOTE_TITLE = "NOTE_TITLE"
        private const val NOTE_DESCRIPTION = "NOTE_DESCRIPTION"

        fun newInstance(note: Note): NoteFragment {
            val fragment = NoteFragment()
            val arguments = Bundle()
            arguments.apply {
                putString(NOTE_TITLE, note.name)
                putString(NOTE_DESCRIPTION, note.description)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title_note_text_view.text = arguments?.getString(NOTE_TITLE, "FAIL")
        description_note_text_view.text = arguments?.getString(NOTE_DESCRIPTION, "DESC")


        delete_btn.setOnClickListener {
            showAlter()
        }

        edit_btn.setOnClickListener {
            (activity as MainActivity).loadFragment(EditNoteFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlter() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Удалить запись?")
        builder.setPositiveButton("Да") { _, _ ->
            //    viewModel.delete()

            (activity as MainActivity).loadFragment(NotesFragment())
        }
        builder.setNegativeButton("Отмена") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
