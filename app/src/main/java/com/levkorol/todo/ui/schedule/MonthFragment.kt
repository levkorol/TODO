package com.levkorol.todo.ui.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.utils.isMounth
import com.levkorol.todo.utils.isSameWeek
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.fragment_month.*


class MonthFragment : Fragment() {

    private lateinit var adapter: ScheduleAdapter
    private var schedule: Schedule? = null
    private lateinit var viewModel: ScheduleViewModel
    private var id: Long = -1
    private var time: Long = -1
    private var schedules: List<Schedule>? = null
    private var pagePosition: Int = 0

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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_mounth)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = ScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter


        val calendarView = calendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = StringBuilder()
                .append(dayOfMonth).append("-")
                .append(month + 1)
                .append("-").append(year)
                .append(" ").toString()
          //  tv_date.text = selectedDate
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel =
            ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()

    }


    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
            updateSchedules()
        })
    }

    private fun updateSchedules() {
        if (schedules == null) return
        adapter.dataItems = schedules!!.filter { schedule ->
            when (pagePosition) {
                0 -> isMounth(schedule.date)
        //       1 -> isToday(schedule.date)
//                2 -> isSameWeek(schedule.date)

                else -> true
            }
        }
        adapter.notifyDataSetChanged()
    }
}
