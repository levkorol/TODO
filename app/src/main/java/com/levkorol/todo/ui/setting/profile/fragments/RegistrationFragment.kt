package com.levkorol.todo.ui.setting.profile.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.levkorol.todo.R
import com.levkorol.todo.model.User
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseDatabase.getInstance().reference

        validateChar()

        signUp.setOnClickListener {
            createUser()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun createUser() {
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (validate(email, password, name)) {
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result?.signInMethods?.isEmpty() != false) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val user = User(name = name, email = email)
                                    val reference = dataBase
                                        .child("users")
                                        .child(it.result?.user?.uid.toString())
                                    reference.setValue(user)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                replaceFragment(
                                                    ProfileFragment()
                                                )
                                            } else {
                                                showToast("Ошибка со входом в профиль. Попробуйте снова.")
                                            }
                                        }
                                } else {
                                    showToast("Ошибка в создании пользователя. Попробуйте снова.")
                                }
                            }
                    } else {
                        showToast("Такой емеил уже зарегестрирован.")
                    }
                }
            }
        } else {
            showToast("Для регистрации все поля должны быть заполнены.")
        }
    }

    private fun validate(email: String, password: String, name: String) =
        email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()

    private fun validateChar() {
        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (editTextPassword.text!!.length < 6) {
                    editTextPassword.error = "6 characters minimum"
                }
                if (editTextEmail.text!!.length < 6 || !editTextEmail.text.toString().contains(
                        "@"
                    )
                ) {
                    editTextEmail.error = "Enter email"
                }
            }
        })
    }

    private fun showToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }
}
