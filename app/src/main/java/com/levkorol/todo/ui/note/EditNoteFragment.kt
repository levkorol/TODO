package com.levkorol.todo.ui.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesViewModel
import kotlinx.android.synthetic.main.add_note.back_profile
import kotlinx.android.synthetic.main.edit_note_fragment.*
import kotlinx.android.synthetic.main.fragment_note.*
import java.io.File
import java.util.*

class EditNoteFragment : Fragment() {
    private var noteId: Long = -1
    private lateinit var viewModel: NotesViewModel
    private var note: Note? = null
    private var photoUri: Uri? = null

    companion object {
        private const val PICK_IMAGE = 100
        private const val CAMERA_INTENT = 12
        private const val NOTE_ID = "NOTE_ID"
        private const val PHOTO = "Photo"

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
        initViews()
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        observeNotes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            photoUri = data!!.data
            photoViewEdit.setImageURI(photoUri)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_INTENT) {
            photoViewEdit.setImageURI(photoUri)
        }
    }

    private fun initViews() {

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        edit_save_note_btn.setOnClickListener {
            Toast.makeText(activity, "Изменения сохранены", Toast.LENGTH_LONG).show()
            saveEditNote()
            parentFragmentManager.popBackStack()
            (activity as MainActivity).loadFragment(NoteFragment.newInstance(note!!))
        }

        star_ed.setOnClickListener {
            updateStar()
        }

        photoViewEdit.setOnClickListener {
            showAlterDialog()
        }
    }

    private fun saveEditNote() {
        // val oldNote = note?.copy()
        note!!.name = edit_title_text.text.toString()
        note!!.description = edit_description_note_text.text.toString()
        note!!.star = star_ed.isSelected
        note!!.photo = photoUri.toString()
//        if (oldNote != note) {
//          showAlterExit()
//        }
        MainRepository.update(note!!)

    }

    private fun observeNotes() {
        viewModel.getDeprecatedNotes().observe(this, Observer<List<Note>> { notes ->
            note = notes.firstOrNull { n -> n.id == noteId }
            if (note != null) {
                edit_title_text.setText(note?.name)
                edit_description_note_text.text = SpannableStringBuilder(note?.description)

                 //TODO  передать сохраненную картинку если она есть в режим редактирования
                // TODO и если ее не менять и не удалить что бы сохранилась при сохранении редактирования= нужон note.photo

                val photo = Uri.parse(note!!.photo)
                photoViewEdit.setImageURI(photo)

                star_ed.isSelected = note!!.star
                if (star_ed.isSelected) {
                    star_ed.setImageResource(R.drawable.ic_star)
                } else {
                    star_ed.setImageResource(R.drawable.ic_star_in_add_notes)
                }
            }
        })
    }

    private fun updateStar() {
        if (star_ed.isSelected) {
            star_ed.setImageResource(R.drawable.ic_star_in_add_notes)
            star_ed.isSelected = false
        } else {
            star_ed.setImageResource(R.drawable.ic_star)
            star_ed.isSelected = true
        }
    }

    private fun getNewPhotoUri(): Uri {
        val photosDir = File(requireContext().filesDir, "photos")
        if (!photosDir.exists()) photosDir.mkdir()
        val photoFile = File(photosDir, UUID.randomUUID().toString())
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(photoFile)
        } else {
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                photoFile
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun openCamera() {
        photoUri = getNewPhotoUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, CAMERA_INTENT)
    }

    // @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        //  builder.setMessage("Откуда вы хотите загрузить изображение?")
        builder.setTitle("Откуда вы хотите загрузить изображение?")
        val pictureDialogItems = arrayOf("Галлерея", "Камера")
        builder.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }
        builder.show()


//        builder.setNegativeButton("Галлерея") { _, _ ->
//            openGallery()
//        }
//        builder.setPositiveButton("Камера") { _, _ ->
//            openCamera()
//        }
//        builder.setNeutralButton("Отменить") { _, _ ->
//        }
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
    }
//
//    @SuppressLint("UseRequireInsteadOfGet")
//    private fun showAlterExit() {
//        val builder = AlertDialog.Builder(context!!)
//        builder.setMessage("Сохранить изменения?")
//        builder.setPositiveButton("Да") { _, _ ->
//            NoteRepository.update(note!!)
//            parentFragmentManager.popBackStack()
//            // (activity as MainActivity).loadFragment(NotesFragment())
//        }
//        builder.setNegativeButton("Отмена") { _, _ ->
//
//        }
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }

}
