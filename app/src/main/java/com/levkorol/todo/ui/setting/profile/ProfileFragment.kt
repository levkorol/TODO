package com.levkorol.todo.ui.setting.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.levkorol.todo.R
import com.levkorol.todo.model.User
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var user: User
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private lateinit var mDataBase: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

       // auth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference
        mDataBase.child("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                user = it.getValue(User::class.java)!!
                hello.text = "Привет, ${user.name} ! :) \n"
            })
    }

    override fun onStart() {
        super.onStart()
    }

    private fun shoeAlertExit() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Хотите выйти из аккаунта?")
        builder.setPositiveButton("Да") { dialog, which ->
            auth.signOut()
            // (activity as MainActivity).loadFragment(AuthorizationFragment())
            parentFragmentManager.popBackStack() //todo ne rab
        }
        builder.setNegativeButton("Отмена") { dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
//
//    private fun loadInfo() {
//        db.collection("users")
//            .whereEqualTo("email", auth.currentUser?.email)
//            .get()
//            .addOnSuccessListener { result ->
//                if (result.documents.size == 0) {
//                    Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
//                } else {
//                    updateLayout(result.documents[0])
//                }
//            }
//            .addOnFailureListener { exception ->
//
//            }
//    }
//
//    private fun updateLayout(document: DocumentSnapshot) {
//        val name = document.get("name").toString()
//        hello.text = "Привет, ${name} !"
//    }
}

