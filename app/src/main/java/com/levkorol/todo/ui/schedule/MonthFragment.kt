package com.levkorol.todo.ui.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.levkorol.todo.R
import kotlinx.android.synthetic.main.fragment_month.*


class MonthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = calendarView // as CalendarView
	calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
        val selectedDate = StringBuilder().append(month + 1)
            .append("-").append(dayOfMonth).append("-").append(year)
            .append(" ").toString()
        Toast.makeText(context, selectedDate, Toast.LENGTH_LONG).show()
    }
    }
}
