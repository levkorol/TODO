package com.levkorol.todo.ui.setting.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.authorization.*


class AuthorizationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        window.decorView.systemUiVisibility =
//            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        registrBtn.setOnClickListener {
            (activity as MainActivity).loadFragment(RegistrationFragment())
        }
    }
}
