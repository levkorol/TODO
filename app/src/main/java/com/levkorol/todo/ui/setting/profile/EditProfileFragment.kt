package com.levkorol.todo.ui.setting.profile


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.levkorol.todo.R
import com.levkorol.todo.model.User
import kotlinx.android.synthetic.main.dialog_password.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {

    private lateinit var mUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference
    private lateinit var pendingUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference
        mDataBase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                et_name.setText(mUser.name, TextView.BufferType.EDITABLE)
                et_email.setText(mUser.email, TextView.BufferType.EDITABLE)
            })

        save.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        pendingUser = User(
            name = et_name.text.toString(),
            email = et_email.text.toString()
        )

        val error = validate(pendingUser)
        if (error == null) {
            if (pendingUser.email == mUser.email) {
                updateUser(pendingUser)
            } else {
                val view = requireActivity().layoutInflater.inflate(R.layout.dialog_password, null)
                AlertDialog.Builder(requireContext())
                    .setView(view)
                    .setPositiveButton("Да") { _, _ ->
                        onPasswordConfim(view.password_input.text.toString())
                    }
                    .setNegativeButton("Отмена") { _, _ -> }
                    .setTitle("Введите пароль для смены емеила")
                    .create()
                    .show()
            }
        } else {
            showToast(error)
        }
    }

    private fun onPasswordConfim(password: String) {
        Log.d("EditProfileFragment", "password : $password")
        if (password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    mAuth.currentUser!!.updateEmail(pendingUser.email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            updateUser(pendingUser)
                        } else {
                            showToast(it.exception!!.message!!)
                        }
                    }
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
        } else {
            showToast("Пароль не был введен.")
        }
    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.email != mUser.email) updatesMap["email"] = user.email
        mDataBase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updatesMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Профиль сохранен")
                    parentFragmentManager.popBackStack()
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
    }

    private fun validate(user: User): String? =
        when {
            user.name.isEmpty() -> "Пожалуйста введите имя"
            user.email.isEmpty() -> "Пожалуйста введите емаил"
            else -> null
        }

    private fun showToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }
}

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
        Log.d("EditProfileFragment", "onCanceled", error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }
}
