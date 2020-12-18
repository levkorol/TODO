package com.levkorol.todo.ui.setting.pincode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.utils.hideKeyboard
import com.levkorol.todo.utils.showToast
import kotlinx.android.synthetic.main.fragment_add_pin.*

class AddPinFragment : Fragment() {

    private val userRepo = DataProvider.userRepo
    private var visibleFlag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        saved_pin.setOnClickListener {
            if (userRepo.getPinCode() != "") {
                requireContext().showToast("${userRepo.getPinCode()}")
            } else {
                requireContext().showToast("У вас еще нет сохраненного пароля")
            }
        }

//        add_new_pin.setOnClickListener {
//            if (visibleFlag){
//                visibleFlag = false
//                linear_add_new_pin.visibility = View.GONE
//            }
//            if (!visibleFlag) {
//                visibleFlag = true
//                linear_add_new_pin.visibility = View.VISIBLE
//            }
//        }

        btnSavePinCode.setOnClickListener {
            checkPin(vPinCode.text.toString(), vPinCodeRepeat.text.toString())
        }

        switch_pin.isChecked = userRepo.needToRequestPinCode
        switch_pin.setOnClickListener {
            if (userRepo.getPinCode() == "") {
                requireContext().showToast("Создайте сначала пароль")
                switch_pin.isChecked = false
            } else {
                userRepo.needToRequestPinCode = !userRepo.needToRequestPinCode
            }
        }
    }

    private fun checkPin(pin: String, pinRepeat: String) {
        if (pin == pinRepeat) {
            saveToDb(pin)
            requireContext().showToast("Пароль сохранен")
            hideKeyboard()
            //linear_add_new_pin.visibility = View.GONE
        } else {
            requireContext().showToast("Пароли не совпадают")
        }
    }

    private fun saveToDb(pin: String) {
        userRepo.setPinCode(pin)
    }

//   private fun changeIc() {
//       val titleRes =
//           if (visibleFlag) R.drawable. else R.drawable.ic_question_no_activ
//       titleImage.setImageResource(titleRes)
//   }
//

}
