package com.levkorol.todo.ui.note

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Note
import kotlinx.android.synthetic.main.add_note.*
import java.util.*
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import androidx.core.content.FileProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.add_note.add_title_text
import kotlinx.android.synthetic.main.add_note.back_profile
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.io.File
import java.text.SimpleDateFormat


class AddNoteFragment : Fragment() {

    private var flagStar: Boolean = false
    var photoUri: Uri? = null
    private lateinit var note: Note
    private var parentFolderId: Long = 0
    private var dateDateSchedule: Long = System.currentTimeMillis()
    private var time: Long = System.currentTimeMillis()
    private var addScheduleFlag = false
    private var addPhoto = false
    private var noteId: Long = 1


    companion object {
        private val TAG = AddNoteFragment::class.java.simpleName
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
        if (arguments != null) {
            parentFolderId = arguments?.getLong(PARENT_FOLDER, 0)!!
        }
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            photoUri = data!!.data
            photoView.setImageURI(photoUri)
            addPhoto = true
            deletePhoto.visibility = View.VISIBLE
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_INTENT) {
            photoView.setImageURI(photoUri)
            addPhoto = true
            deletePhoto.visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        save_note_btn.setOnClickListener {
            saveNote()
            parentFragmentManager.popBackStack()
            (activity as MainActivity).loadFragment(NoteFragment.instance(note.id))
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
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<Long> = builder.build()
            picker.addOnPositiveButtonClickListener { unixTime ->
                text_date_time.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(unixTime))
                dateDateSchedule = unixTime
                addScheduleFlag = true
                deleteSchedule.visibility = View.VISIBLE
            }
            picker.show(parentFragmentManager, picker.toString())

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                text_date.visibility = View.VISIBLE
                addScheduleFlag = true
                text_date.text = SimpleDateFormat("HH:mm").format(cal.time)
                time = cal.time.time
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        deleteSchedule.setOnClickListener {
            deleteSchedule.visibility = View.GONE
            addScheduleFlag = false
            text_date.visibility = View.GONE
            text_date_time.text = "Добавить в расписание"
        }

        deletePhoto.setOnClickListener {
            photoUri = null
            addPhoto = false
            photoView.setImageResource(R.drawable.ic_add_photo)
            deletePhoto.visibility = View.GONE
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
        note = Note(
            name = add_title_text.text.toString(),
            description = add_description_note_text.text.toString(),
            star = star_image_btn.isSelected,
            photo = photoUri.toString(),
            date = System.currentTimeMillis(),
            parentFolderId = parentFolderId,
            time = time,
            dateSchedule = dateDateSchedule,
            alarm = false,
            addSchedule = addScheduleFlag,
            addPhoto = addPhoto
        )
        MainRepository.addNote(note)
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


