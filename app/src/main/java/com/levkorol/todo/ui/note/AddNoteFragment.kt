package com.levkorol.todo.ui.note

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesFragment
import kotlinx.android.synthetic.main.add_note.*
import java.util.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.levkorol.todo.DatePickerFragment
import com.levkorol.todo.R
import com.levkorol.todo.utils.*
import kotlinx.android.synthetic.main.dialog_data.*
import java.io.File
import java.io.IOException


class AddNoteFragment : Fragment() {

    private var flagStar: Boolean = false
    var photoUri: Uri? = null
    var note: Note? = null
    private var parentFolderId: Long = 0

    companion object {
        private const val PICK_IMAGE = 100
        private const val CAMERA_INTENT = 12
        private val REQUEST_DATE = 0
        private val DIALOG_DATE = "DialogDate"
        private const val PARENT_FOLDER = "ParentFolder"

        fun newInstance(parentFolderId: Long): AddNoteFragment {
            val fragment = AddNoteFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(PARENT_FOLDER, parentFolderId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null) {
            parentFolderId = arguments?.getLong(PARENT_FOLDER, 0)!!
        }
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
                photoUri = data!!.data
                photoView.setImageURI(photoUri)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_INTENT) {
            photoView.setImageURI(photoUri)
        }
    }

    private fun initViews() {
        save_note_btn.setOnClickListener {
            (activity as MainActivity).loadFragment(NotesFragment())
            saveNote()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        star_image_btn.setOnClickListener {
            starClick()

        }

        photoView.setOnClickListener {
            showAlterDialog()
        }

        addSchedule.setOnClickListener {

        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun openCamera() {
        photoUri = getNewPhotoUri()
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        intent.putExtra(EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, CAMERA_INTENT)
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
                "Вы создали заметку",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun saveNote() {
        NoteRepository.addNote(
            Note(
                name = add_title_text.text.toString(),
                description = add_description_note_text.text.toString(),
                star = star_image_btn.isSelected,
                photo = photoUri.toString(),
                date = 1,
                parentFolderId = parentFolderId
            )
        )
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

    private fun starClick() {
        if (flagStar) {
            star_image_btn.setImageResource(com.levkorol.todo.R.drawable.ic_star_in_add_notes)
            star_image_btn.isSelected = false
            flagStar = false
        } else {
            star_image_btn.setImageResource(com.levkorol.todo.R.drawable.ic_star)
            Toast.makeText(activity, "Вы отметили заметку как важная", Toast.LENGTH_LONG).show()
            star_image_btn.isSelected = true
            flagStar = true
        }
    }

    private fun getNewPhotoUri(): Uri {
        val photosDir = File(requireContext().filesDir, "photos")
        if (!photosDir.exists()) photosDir.mkdir()
        val photoFile = File(photosDir, UUID.randomUUID().toString())
        return if (SDK_INT < N) {
            Uri.fromFile(photoFile)
        } else {
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                photoFile
            )
        }
    }

    private fun validations(): Boolean {
        return !(add_title_text.text.isNullOrEmpty()
                && add_description_note_text.text.isNullOrEmpty()
                )
    }
}


