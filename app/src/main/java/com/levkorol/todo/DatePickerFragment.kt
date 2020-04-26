package com.levkorol.todo

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {


    private var mDatePicker: DatePicker? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_data, null)

        mDatePicker = v.findViewById<View>(R.id.dialog_date_picker) as DatePicker
        mDatePicker!!.init(year, month, day, null)

        return AlertDialog.Builder(activity)
            .setView(v)
            .setTitle("Выберите дату")
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                val year = mDatePicker!!.year
                val month = mDatePicker!!.month
                val day = mDatePicker!!.dayOfMonth
                val date = GregorianCalendar(year, month, day).time
                sendResult(Activity.RESULT_OK, date)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        val EXTRA_DATE = "com.levkorol.android.criminalintent.date"
        private val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)

            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
