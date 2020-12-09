package com.levkorol.todo.ui.setting.pincode

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.data.user.UserRepo
import com.levkorol.todo.utils.showToast
import kotlinx.android.synthetic.main.activity_pin_code.*


class PinCodeActivity : AppCompatActivity() {

    private val userRepo: UserRepo by lazy { DataProvider.userRepo }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)
        vEnterPinCode.setOnPinEnteredListener { pin ->
            checkPinCode(pin.toString())
        }
    }

    private fun checkPinCode(enteredPinCode: String) {
        if (enteredPinCode == userRepo.getPinCode()) {
//            replaceFragment(ScheduleFragment())

            // TODO добавить навигацию
            startActivity(Intent(this, MainActivity::class.java))

        } else {
            this.showToast("Пин-код введен не верно")
        }
    }
}