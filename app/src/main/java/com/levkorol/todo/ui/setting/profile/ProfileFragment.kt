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
import com.levkorol.todo.model.User
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var user: User
    private lateinit var mDataBase: DatabaseReference
    private val folders = MainRepository.getResultFolders().value
    private val notes = MainRepository.getNotes().value
    private val schedules = MainRepository.getSchedules().value

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
            // TODO в корутине!
            // TODO использовать get*Now()
            val data = mapOf(
                "folders" to folders,
                "notes" to notes,
                "schedules" to schedules   //todo schedulы не сохраняются. остальное вроде ок
            )

            mDataBase.child("users").child(auth.currentUser!!.uid).child("notes")
                .updateChildren(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "Данные сохранены", Toast.LENGTH_SHORT)
                            .show() //todo почему тоаст не показывается при сохранении
                    } else {
                        Toast.makeText(
                            activity,
                            "Произошла ошибка. Попробуйте позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        restore_btn.setOnClickListener {
            // TODO получаем из "notes"/"USER-ID" List<Base>
            // TODO в репозитории (удаляем всё) добавляем то что получили с сервера

            folders?.let { folders -> MainRepository.deleteFolders(folders) } // TODO заменить на deleteAll
            notes?.let { it1 -> MainRepository.deleteNotes(it1) } // TODO
            mDataBase.child("users").child(auth.currentUser!!.uid).child("notes")
                .addListenerForSingleValueEvent(ValueEventListenerAdapter { notes ->
                    // TODO ??????
                })
//            folders?.let { it1 ->
//                MainRepository.getFolder().value
//            } //todo или нужно в репоситории создать метод добавляющий лист?
//            notes?.let { it1 -> MainRepository.getNotes().value }
        }

        mDataBase.child("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                user = it.getValue(User::class.java)!!
                hello.text = "Привет, ${user.name} ! :) \n"
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

