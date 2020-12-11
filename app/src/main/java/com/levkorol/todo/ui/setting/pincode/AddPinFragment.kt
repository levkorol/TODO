package com.levkorol.todo.ui.setting.pincode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.utils.showToast
import kotlinx.android.synthetic.main.fragment_add_pin.*

class AddPinFragment : Fragment() {

    private val userRepo = DataProvider.userRepo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userRepo.getPinCode() != "") {
            saved_pin.text = "Текущий сохраненный пароль: ${userRepo.getPinCode()}"
        }

        btnSavePinCode.setOnClickListener {
            checkPin(vPinCode.text.toString(), vPinCodeRepeat.text.toString())
        }

        switch_pin.isChecked = userRepo.needToRequestPinCode
        switch_pin.setOnClickListener {
            userRepo.needToRequestPinCode = !userRepo.needToRequestPinCode
        }
    }

    private fun checkPin(pin: String, pinRepeat: String) {
        // проверить одинаковость
        if (pin == pinRepeat) {
            saveToDb(pin)
            saved_pin.text = "Текущий сохраненный пароль: ${userRepo.getPinCode()}"
            requireContext().showToast("Пин-код сохранен")
        } else {
            requireContext().showToast("Пин-коды не совпадают")
        }
    }

    private fun saveToDb(pin: String) {
        userRepo.setPinCode(pin)
    }

}
