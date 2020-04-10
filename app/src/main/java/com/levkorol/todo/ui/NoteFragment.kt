package com.levkorol.todo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.levkorol.todo.R

class NoteFragment : Fragment() {

    companion object {
        private const val NOTE_ID = "NOTE_ID"
        fun newInstance(noteId: Long): NoteFragment {
            val fragment = NoteFragment()
            val arguments = Bundle()
            arguments.putLong(NOTE_ID, noteId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }


}
