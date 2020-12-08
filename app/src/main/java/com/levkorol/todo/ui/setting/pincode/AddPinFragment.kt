package com.levkorol.todo.ui.setting.pincode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R

class AddPinFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//       val pin = Pinview(this)
//        pin.setPinBackgroundRes(R.drawable.sample_background);
//        pin.setPinHeight(40);
//        pin.setPinWidth(40);
//        pin.setInputType(Pinview.InputType.NUMBER);
//        pin.setValue("1234");

    }

    private fun checkPin(pin: String, pinRepeat: String): Boolean {
        // проверить одинаковость
        return if (pin == pinRepeat) {
            saveToDb(pin)
            true
        } else {
            false
        }
    }

    private fun saveToDb(pin: String) {
        // todo сохранить в базу
    }

}
