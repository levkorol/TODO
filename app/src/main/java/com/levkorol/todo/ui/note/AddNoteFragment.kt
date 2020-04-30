package com.levkorol.todo.ui.note

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.utils.format
import kotlinx.android.synthetic.main.add_note.*
import java.util.*
import android.graphics.Bitmap
import com.levkorol.todo.utils.convertBitmapToByteArray
import com.levkorol.todo.utils.convertToString
import java.io.IOException


class AddNoteFragment : Fragment() {
    private var flagStar: Boolean = false
    var photoBitmap: Bitmap? = null // > AddNoteViewModel  позже

    companion object {
        private const val PICK_IMAGE = 100
        private const val CAMERA_INTENT = 12

    }

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

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        star_image_btn.setOnClickListener {
            starClick()

        }

        photoView.setOnClickListener {
            showAlterDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                photoBitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data)
                photoView.setImageBitmap(photoBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_INTENT) {
//            val uri = FileProvider.getUriForFile(requireActivity(),
//                "", cameraPhotoFile!!)
//            requireActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun openCamera() {
        startActivityForResult(Intent(ACTION_IMAGE_CAPTURE), CAMERA_INTENT)
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
        var imageString: String = ""
        if (photoBitmap != null) {
            val byteArray = convertBitmapToByteArray(photoBitmap!!)
            photoBitmap!!.recycle()
            imageString = convertToString(byteArray)
        }
        NoteRepository.addNote(
            Note(
                name = add_title_text.text.toString(),
                description = add_description_note_text.text.toString(),
                star = star_image_btn.isSelected,
                photo = imageString,
                date = Date().format()
            )
        )

    }

    private fun validations(): Boolean {
        return !(add_title_text.text.isNullOrEmpty()
                && add_description_note_text.text.isNullOrEmpty()
                )
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlterDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Откуда вы хотите загрузить изображение?")

        builder.setNegativeButton("Камера") { _, _ ->
            openCamera()
        }
        builder.setPositiveButton("Галлерея") { _, _ ->
            openGallery()
        }
        builder.setNeutralButton("Отменить") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun starClick() {
        if (flagStar) {
            star_image_btn.setImageResource(R.drawable.ic_star_in_add_notes)
            star_image_btn.isSelected = false
            flagStar = false
        } else {
            star_image_btn.setImageResource(R.drawable.ic_star)
            Toast.makeText(activity, "Вы отметили заметку как важная", Toast.LENGTH_LONG).show()
            star_image_btn.isSelected = true
            flagStar = true
        }
    }
}


