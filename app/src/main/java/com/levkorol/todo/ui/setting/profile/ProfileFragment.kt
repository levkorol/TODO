package com.levkorol.todo.ui.setting.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val TAG = "Auth"
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

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
            if(it.currentUser == null) {
                (activity as MainActivity).loadFragment(AuthorizationFragment())
            }
        }

        edit.setOnClickListener {
            (activity as MainActivity).loadFragment(EditProfileFragment())
        }

       // auth.signOut()
//        auth.signInWithEmailAndPassword("lev@lev.com", "123456")
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Log.d(TAG, "signin")
//                } else {
//                    Log.d(TAG, "fail")
//                }
//            }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun shoeAlertExit() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Хотите выйти из аккаунта?")
        builder.setPositiveButton("Да"){dialog, which ->
            auth.signOut()
           // (activity as MainActivity).loadFragment(AuthorizationFragment())
           parentFragmentManager.popBackStack() //todo ne rab
        }
        builder.setNegativeButton("Отмена"){dialog,which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
