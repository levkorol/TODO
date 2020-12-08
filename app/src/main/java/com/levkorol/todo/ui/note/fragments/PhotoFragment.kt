package com.levkorol.todo.ui.note.fragments


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.model.Note
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
