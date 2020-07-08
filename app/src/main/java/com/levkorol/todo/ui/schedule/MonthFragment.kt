package com.levkorol.todo.ui.schedule


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Note
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.fragment_month.*
import java.util.*

class MonthFragment : Fragment() {

    private var schedule: Schedule? = null
    private lateinit var viewModel: ScheduleViewModel
    private var schedules: List<Schedule>? = null
    private var id: Long = -1
    private var time: Long = -1
    private var valueSchedule = MainRepository.getSchedules().value

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
            schedule?.time = arguments?.getLong(TIME, time)!!
        }

        //todo как окрасть даты или поставить счетчик где есть события(здесь по сути иконки даже можно)
        // TODO создать массив
//        val calendar = Calendar.getInstance()
        // TODO calendar.timeInMillis =
//        events.add(EventDay(calendar, R.drawable.ic_delete))
//        events.add(EventDay(calendar, Drawable()))
//        events.add(EventDay(calendar, R.drawable.ic_delete,parseColor("#228B22")))
//        val calendarViewM = findViewById(R.id.calendarView) as CalendarView
//        calendarViewM.setEvents(events)

        calendarViewM.setOnDayClickListener(object : OnDayClickListener {
            @Override
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar // TODO .timeInMillis
                val selectedDates = calendarViewM.selectedDates
                val calendar = Calendar.getInstance()
                val thenYear = calendar.get(Calendar.YEAR)
                val thenMonth = calendar.get(Calendar.MONTH)
                val thenDay = calendar.get(Calendar.DAY_OF_MONTH)
                val date = calendar.set(thenYear, thenMonth, thenDay)
                calendarViewM.setDate(calendar)

               //todo как вытащить лонг дату с этого календаря
                val builder = MaterialAlertDialogBuilder(requireContext())
                builder.setTitle("Выбранная дата:")
                builder.setMessage("")
                builder.setPositiveButton("Просмотреть") { _, _ ->
                    (activity as MainActivity).loadFragment(AllScheduleListFragment())
                }
                builder.setNegativeButton("Добавить задачу") { _, _ ->
                    (activity as MainActivity).loadFragment(AddScheduleFragment.newInstance(1))
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
//        observeSchedule()
    }

//    private fun observeSchedule() {
//        viewModel.getSchedules().observe(this, Observer { schedules ->
//
//        })
//    }
}


//        val calendarView = calendarView
//        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            val selectedDate = "$dayOfMonth.${month + 1}.$year"
//            val selectedDateLong = calendarView.date
////            val calendar = Calendar.getInstance()
////            calendar.timeInMillis = selectedDateLong
////            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
////            val a = dateFormatter.format(calendar.time)
////            val date = Tools.convertDateToLong(a)
//
//            val builder = MaterialAlertDialogBuilder(requireContext())
//            builder.setTitle("Выбранная дата:")
//            builder.setMessage(selectedDate)
//            builder.setPositiveButton("Просмотреть") { _, _ ->
//                (activity as MainActivity).loadFragment(AllScheduleListFragment())
//            }
//            builder.setNegativeButton("Добавить задачу") { _, _ ->
//                (activity as MainActivity).loadFragment(AddScheduleFragment.newInstance(selectedDateLong))
//            }
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
//        }