package com.levkorol.todo.ui.setting.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.model.User
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var user: User
    private lateinit var mDataBase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDataBase = FirebaseDatabase.getInstance().reference

        exit.setOnClickListener {
            shoeAlertExit()
        }

        auth.addAuthStateListener {
            if (it.currentUser == null) {
                (activity as MainActivity).loadFragment(AuthorizationFragment())
            }
        }

        edit.setOnClickListener {
            (activity as MainActivity).loadFragment(EditProfileFragment())
        }

        save_btn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val notes = MainRepository.getNotesNow()
                val schedules = MainRepository.getAllSchedulesNow()
                val folders = MainRepository.getAllFoldersNow()
                val data = mapOf(
                    "folders" to folders,
                    "notes" to notes,
                    "schedules" to schedules
                )
                mDataBase.child("users").child(auth.currentUser!!.uid).child("notes")
                    .updateChildren(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Данные сохранены", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                activity,
                                "Произошла ошибка. Попробуйте позже",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        restore_btn.setOnClickListener {

            vostanovleno.text = "Поздравляем!\n Ваши записи успешно восстановлены! "

            MainRepository.deleteAllFolders()
            MainRepository.deleteAllNotes()
            MainRepository.deleteAllSchedules()

            mDataBase.child("users").child(auth.currentUser!!.uid).child("notes")
                .addListenerForSingleValueEvent(ValueEventListenerAdapter { notes ->

                    val childNotes = notes.child("notes").children
                    childNotes.forEach { firebaseNote ->
                        val realNote = firebaseNote.getValue(Note::class.java)
                        realNote?.let { note -> MainRepository.addNote(note) {} }
                    }

                    val childFolders = notes.child("folders").children
                    childFolders.forEach { firebaseFolder ->
                        val realFolder = firebaseFolder.getValue(Folder::class.java)
                        realFolder?.let { folder -> MainRepository.addFolder(folder) }
                    }

                    val childSchedule = notes.child("schedules").children //WTF?! как у тебя восстанавливалось расписание?
                    childSchedule.forEach { firebaseSchedules ->
                        val realSchedule = firebaseSchedules.getValue(Schedule::class.java)
                        realSchedule?.let { task -> MainRepository.addSchedule(task) {} }
                    }
                })

            // TODO пока всё что пришло из файербейса - без уведомлений!
        }

        mDataBase.child("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                if(hello != null) {
                    user = it.getValue(User::class.java)!!
                    hello.text = "Привет, ${user.name} ! :) \n"
                }
            })
    }

    private fun shoeAlertExit() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setMessage("Хотите выйти из аккаунта?")
        builder.setPositiveButton("Да") { dialog, which ->
            auth.signOut()
            // (activity as MainActivity).loadFragment(AuthorizationFragment())
            parentFragmentManager.popBackStack()
        }
        builder.setNegativeButton("Отмена") { dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

