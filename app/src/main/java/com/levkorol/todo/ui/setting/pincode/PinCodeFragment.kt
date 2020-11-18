package com.levkorol.todo.ui.setting.pincode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import kotlinx.android.synthetic.main.fragment_pin_code.*

/**
 * A simple [Fragment] subclass.
 */
class PinCodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBtnPinCode.setOnClickListener {
            checkPinCode(tvPinCode.text.toString())
        }
    }

    private fun checkPinCode(enteredPinCode: String) {

        //        val savedPin = MainRepository.getPinCode()!!
        //
        //        if (enteredPinCode == savedPin) {
        //            replaceFragment(ScheduleFragment())
        //        } else {
        //            // todo показать что пин введен не верно
        //        }
    }
}
