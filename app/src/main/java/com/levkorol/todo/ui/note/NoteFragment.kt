package com.levkorol.todo.ui.note

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.notes.NotesViewModel
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note.back_profile
import com.levkorol.todo.utils.convertByteArrayToBitmapView
import com.levkorol.todo.utils.convertToByteArrayView
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.android.synthetic.main.edit_note_fragment.*


class NoteFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = -1
    private var flagStar: Boolean = false
    private var note: Note? = null

    companion object {
        private const val NOTE_ID = "NOTE_ID"
        private const val NOTE_TITLE = "NOTE_TITLE"
        private const val NOTE_DESCRIPTION = "NOTE_DESCRIPTION"
        private const val STAR = "STAR"
        private const val PHOTO = "PHOTO"

        fun newInstance(note: Note): NoteFragment {
            val fragment = NoteFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(NOTE_ID, note.id)
                putString(NOTE_TITLE, note.name)
                putString(NOTE_DESCRIPTION, note.description)
                putBoolean(STAR, note.star)
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
        return inflater.inflate(R.layout.fragment_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getLong(NOTE_ID, -1)!!
            //   note =
        title_note_text_view.text = arguments?.getString(NOTE_TITLE, "FAIL")
        description_note_text_view.text = arguments?.getString(NOTE_DESCRIPTION, "DESC")
        star.isSelected = arguments?.getBoolean(STAR, flagStar)!!

        val photoUri = Uri.parse(arguments?.getString(PHOTO, "photo"))
        imageViewNotePhoto.setImageURI(photoUri)


        if (star.isSelected) {
            star.setImageResource(R.drawable.ic_star)
        } else {
            star.setImageResource(R.drawable.ic_star_in_add_notes)
        }

        delete_btn.setOnClickListener {
            showAlter()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        edit_btn.setOnClickListener {
            (activity as MainActivity).loadFragment(EditNoteFragment.newInstance(noteId))
        }

        share_btn.setOnClickListener {
            shareNote()
        }

        imageViewNotePhoto.visibility = if (imageViewNotePhoto == null) View.GONE else View.VISIBLE

        imageViewNotePhoto.setOnClickListener {
            if (imageViewNotePhoto != null) (activity as MainActivity).loadFragment(
                PhotoFragment.newInstance(
                    note!!
                )
            )
        }
    }

    private fun observeNotes() {
        viewModel.getDeprecatedNotes().observe(this, Observer<List<Note>> { notes ->
            note = notes.firstOrNull { n -> n.id == noteId }
        })
    }


    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        observeNotes()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlter() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Удалить запись?")
        builder.setPositiveButton("Да") { _, _ ->
            NoteRepository.deleteById(noteId)
            (activity as MainActivity).loadFragment(NotesFragment())
        }
        builder.setNegativeButton("Отмена") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun shareNote() {
        val title = title_note_text_view.text.toString()
        val desc = description_note_text_view.text.toString()
        val s = title + "\n" + desc
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plan"
        shareIntent.putExtra(Intent.EXTRA_TEXT, s)
        startActivity(Intent.createChooser(shareIntent, s))
    }
}
