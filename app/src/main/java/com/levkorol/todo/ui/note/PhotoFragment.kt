package com.levkorol.todo.ui.note


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.levkorol.todo.R
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.notes.NotesViewModel
import com.levkorol.todo.utils.convertByteArrayToBitmapView
import com.levkorol.todo.utils.convertToByteArrayView
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_photo.*


class PhotoFragment : Fragment() {
    private var noteId: Long = -1

    companion object {

        private const val NOTE_ID = "NOTE_ID"
        private const val PHOTO = "PHOTO"

        fun newInstance(note: Note): PhotoFragment {
            val fragment = PhotoFragment()
            val arguments = Bundle()
            arguments.apply {
                //putLong(NOTE_ID, noteId)
                putString(PHOTO, note.photo)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getLong(NOTE_ID, -1)!!

        val photoUri = Uri.parse(arguments?.getString(PHOTO, "photo"))
        iv_photo.setImageURI(photoUri)
    }

}
