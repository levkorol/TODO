package com.levkorol.todo.ui.setting.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
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
            (activity as MainActivity).loadFragment(RegistrationFragment())
        }

//        auth.addAuthStateListener {
//            if(it.currentUser != null) {
//                (activity as MainActivity).loadFragment(ProfileFragment())
//            }
//        }
    }

    fun logIn() {
        val email = login.text.toString()
        val password = password.text.toString()
        if (validate(email, password)) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    (activity as MainActivity).loadFragment(ProfileFragment())
                   //  parentFragmentManager.popBackStack() //todo ne rab
                }
            }
        } else {
            Toast.makeText(activity, "Введите Е-маил и пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()
}
