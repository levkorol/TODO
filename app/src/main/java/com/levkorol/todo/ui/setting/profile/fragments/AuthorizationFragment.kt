package com.levkorol.todo.ui.setting.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.todo.R
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.authorization.*

class AuthorizationFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_button.setOnClickListener {
            logIn()
        }

        registrBtn.setOnClickListener {
            replaceFragment(RegistrationFragment())
        }
    }

    fun logIn() {
        val email = login.text.toString()
        val password = password.text.toString()
        if (validate(email, password)) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    replaceFragment(ProfileFragment())
                    //  parentFragmentManager.popBackStack() //todo ne rab
                }
            }
        } else {
            Toast.makeText(activity, "Введите Е-маил и пароль", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //картинка в статус бар
    }

    override fun onStop() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        super.onStop()

    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()
}
