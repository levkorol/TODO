package com.levkorol.todo.ui.note

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesViewModel
import kotlinx.android.synthetic.main.add_note.back_profile
import kotlinx.android.synthetic.main.edit_note_fragment.*



class EditNoteFragment : Fragment() {

    private var noteId: Long = -1
    private lateinit var viewModel: NotesViewModel
    private var note: Note? = null

    companion object {

        private const val NOTE_ID = "NOTE_ID"

        fun newInstance(noteId: Long): EditNoteFragment {
            val fragment = EditNoteFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(NOTE_ID, noteId)
            }

            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.edit_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getLong(NOTE_ID, -1)!!

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        edit_save_note_btn.setOnClickListener {
            saveEditNote()
            (activity as MainActivity).loadFragment(NoteFragment.newInstance(note!!))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        observeNotes()
    }

    private fun saveEditNote() {
        note!!.name = edit_title_text.text.toString()
        note!!.description = edit_description_note_text.text.toString()
        NoteRepository.update(note!!)
    }

    private fun observeNotes() {
        viewModel.getNotes().observe(this, Observer<List<Note>> { notes ->
            note = notes.firstOrNull { n -> n.noteId == noteId }
            if (note != null) {
                edit_title_text.text = SpannableStringBuilder(note?.name)
                edit_description_note_text.text = SpannableStringBuilder(note?.description)
            }
        })
    }
}
