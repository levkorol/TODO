package com.levkorol.todo.ui.setting.pincode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.levkorol.todo.R
import kotlinx.android.synthetic.main.activity_pin_code.*


class PinCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)
        loginBtnPinCode.setOnClickListener {
            // checkPinCode(pinview.text.toString())
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