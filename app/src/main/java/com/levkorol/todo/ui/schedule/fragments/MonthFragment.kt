package com.levkorol.todo.ui.schedule.fragments

import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.schedule.viewmodel.ScheduleViewModel
import com.levkorol.todo.utils.Tools
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_month.*
import java.util.*

class MonthFragment : Fragment() {

    private var schedule: Schedule? = null
    private lateinit var viewModel: ScheduleViewModel
    private var schedules: List<Schedule>? = null
    private var id: Long = -1
    private var time: Long = -1

    companion object {
        private const val SCHEDULE_ID = "SCHEDULE_ID"
        private const val TIME = "TIME"

        fun newInstance(id: Long): MonthFragment {
            val fragment = MonthFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(SCHEDULE_ID, id)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            id = arguments?.getLong(SCHEDULE_ID, id)!!
            //schedule?.time = arguments?.getLong(TIME, time)!!
        }

        calendarViewM.setOnDayClickListener(object : OnDayClickListener {
            @Override
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar =
                    eventDay.calendar.timeInMillis + eventDay.calendar.timeZone.rawOffset

                val builder = MaterialAlertDialogBuilder(requireContext())
                builder.setTitle("Выбранная дата:")
                builder.setMessage(Tools.dateToString(clickedDayCalendar))
                builder.setPositiveButton("Просмотреть") { _, _ ->
                    replaceFragment(
                        AllScheduleListFragment.newInstance(
                            clickedDayCalendar
                        )
                    )
                }
                builder.setNegativeButton("Добавить задачу") { _, _ ->
                    replaceFragment(
                        AddScheduleFragment.newInstance(
                            clickedDayCalendar
                        )
                    )
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()
    }

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
            val events = arrayListOf<EventDay>()
            for (i in 0..schedules.size - 1) {
                val s = schedules!![i]
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = s.date
                //calendar.set(2020, 6, 7)
                //calendar.setCurrentMonthDayColors()
                events.add(EventDay(calendar, R.drawable.ic_com, parseColor("#228B22")))
                //events.add(EventDay(calendar, R.drawable.ic_no))
              //  events.add(EventDay(calendar, R.drawable.ic_repit, parseColor("#228B22")))
            }
            val calendarViewM = calendarViewM as CalendarView
            calendarViewM.setEvents(events)
        })
    }
}
