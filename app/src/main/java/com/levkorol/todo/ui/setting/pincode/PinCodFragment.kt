package com.levkorol.todo.ui.setting.pincode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.data.user.UserRepo
import com.levkorol.todo.ui.schedule.fragments.ScheduleFragment
import com.levkorol.todo.utils.hideKeyboard
import com.levkorol.todo.utils.replaceFragment
import com.levkorol.todo.utils.showToastf
import kotlinx.android.synthetic.main.fragment_pin_cod.*

class PinCodFragment : Fragment() {

    private val userRepo: UserRepo by lazy { DataProvider.userRepo }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_cod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vEnterPinCode.setOnPinEnteredListener { pin ->
            hideKeyboard()
            checkPinCode(pin.toString())
        }
    }

    private fun checkPinCode(enteredPinCode: String) {
        if (enteredPinCode == userRepo.getPinCode()) {
            hideKeyboard()
            replaceFragment(ScheduleFragment())
        } else {
            showToastf(requireContext(), "Пароль введен не верно")
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setBottomNavigationVisible(false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //картинка в статус бар
    }

    override fun onStop() {
        (activity as? MainActivity)?.setBottomNavigationVisible(true)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        super.onStop()
    }
}