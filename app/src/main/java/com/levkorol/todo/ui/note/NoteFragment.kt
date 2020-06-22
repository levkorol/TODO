package com.levkorol.todo.ui.note

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesViewModel
import com.levkorol.todo.ui.schedule.AlarmReceiver
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.android.synthetic.main.edit_note_fragment.*
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note.back_profile
import kotlinx.android.synthetic.main.fragment_note.text_date


class NoteFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = -1
    private var note: Note? = null
    private lateinit var photoUri: Uri
    private var alarmFlag = false
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    companion object {
        private const val NOTE_ID = "NOTE_ID"
        fun instance(noteId: Long): NoteFragment {
            val fragment = NoteFragment()
            val arg = Bundle()
            arg.apply {
                putLong(NOTE_ID, noteId)
            }
            fragment.arguments = arg
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
        initViews()
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        observeNotes()
    }

    private fun initViews() {

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

        imageViewNotePhoto.setOnClickListener {
            if (note?.addPhoto == true) (activity as MainActivity).loadFragment(
                PhotoFragment.newInstance(
                    note!!
                )
            )
        }

        if (alarmFlag) swich_note_alarm.isChecked = true
        swich_note_alarm.setOnClickListener {
            if (swich_note_alarm.isChecked) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Включить оповещение?")
                builder.setPositiveButton("Да") { dialog, which ->
                    if (swich_note_alarm.isChecked) alarmFlag = true
                    note?.alarm = true
                    note?.let { it1 -> MainRepository.update(it1) }
                    alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        PendingIntent.getBroadcast(context, 0, intent, 0)
                    }
                    if (note?.alarm == true) {
                        alarmManager?.set(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + 60,
                            alarmIntent
                        )
                    }
                }
                builder.setNegativeButton("Отмена") { dialog, which ->
                    note?.alarm = false
                    note?.let { it1 -> MainRepository.update(it1) }
                    swich_note_alarm.isChecked = false
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                swich_note_alarm.isChecked
            }
        }

        star.setOnClickListener {
            updateStar()
        }
    }

    private fun observeNotes() {
        viewModel.getNotes().observe(this, Observer<List<Note>> { notes ->
            note = notes.firstOrNull { n -> n.id == noteId }

            title_note_text_view.text = note?.name
            description_note_text_view.text = note?.description
            if(note?.alarm == true) swich_note_alarm.isChecked = true

            if (note?.star == true) {
                star.isSelected = true
                star.setImageResource(R.drawable.ic_star)
            } else {
                star.setImageResource(R.drawable.ic_star_in_add_notes)
                star.isSelected = false
            }
            if(note?.addPhoto == true ) {
                photoUri = Uri.parse(note!!.photo)
                imageViewNotePhoto.setImageURI(photoUri)
                cardView.visibility = View.VISIBLE
            }
            if (note?.addSchedule == true) {
                timeSchedule.visibility = View.VISIBLE
                alarmSchedule.visibility = View.VISIBLE
                text_date.text = "${note?.date?.let { Tools.dateToStringtwo(it) }}, ${note?.time?.let { Tools.convertLongToTimeString(it) }}"
            }
        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showAlter() {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Удалить запись?")
        builder.setPositiveButton("Да") { _, _ ->
            MainRepository.deleteById(noteId)
            parentFragmentManager.popBackStack()
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

    private fun updateStar() {
        if (star.isSelected) {
            star.setImageResource(R.drawable.ic_star_in_add_notes)
            star.isSelected = false
            note?.star = false
            note?.let { it1 -> MainRepository.update(it1) }
        } else {
            star.setImageResource(R.drawable.ic_star)
            Toast.makeText(activity, "Вы отметили заметку как важная", Toast.LENGTH_LONG).show()
            note?.star = true
            star.isSelected = true
            note?.let { it1 -> MainRepository.update(it1) }
        }
    }
}
