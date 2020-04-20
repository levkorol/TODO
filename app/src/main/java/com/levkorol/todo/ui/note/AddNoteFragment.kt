package com.levkorol.todo.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.notes.NotesViewModel
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {

//    companion object {
//        private const val NOTE_ID = "NOTE_ID"
//        fun newInstance(noteId: Long): AddNoteFragment {
//            val fragment = AddNoteFragment()
//            val arguments = Bundle()
//            arguments.putLong(NOTE_ID, noteId)
//            fragment.arguments = arguments
//            return fragment
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.add_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        save_note_btn.setOnClickListener {
            (activity as MainActivity).loadFragment(NotesFragment())
            saveNote()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteToDatabase()
    }


    private fun saveNoteToDatabase() {
        if (validations()) {
            Toast.makeText(activity, "Заметка успешно сохранена", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(
                activity,
                "Что_то пошло не так. Попробуйте еще раз",
                Toast.LENGTH_SHORT
            ).show()
    }


    private fun validations(): Boolean {
        return !(add_title_text.text.isNullOrEmpty()
                && add_description_note_text.text.isNullOrEmpty()
                )
    }

    private fun saveNote() {
        GlobalScope.launch(Dispatchers.IO) {
            NoteRepository.addNote(
                Note(
                    name = add_title_text.text.toString(),
                    description = add_description_note_text.text.toString()
                )
            )
        }
    }
}
