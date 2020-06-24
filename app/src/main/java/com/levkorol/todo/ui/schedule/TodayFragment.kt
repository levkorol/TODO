package com.levkorol.todo.ui.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.schedule_fragment.*

class TodayFragment : Fragment() {

    private lateinit var adapter: ScheduleAdapter
    private var schedule: Schedule? = null // TODO может это не нужно???
    private lateinit var viewModel: ScheduleViewModel
    private var id: Long = -1 // TODO
    private var time: Long = -1 // TODO
    private var schedules: List<Schedule>? = null
    private var pagePosition: Int = 0 // TODO

    companion object {
        private const val SCHEDULE_ID = "SCHEDULE_ID" // TODO
        private const val TIME = "TIME"

        fun newInstance(id: Long): TodayFragment {
            val fragment = TodayFragment()
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

        return inflater.inflate(R.layout.fragment_today, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            id = arguments?.getLong(SCHEDULE_ID, id)!!
            schedule?.time = arguments?.getLong(TIME, time)!!
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_today)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = ScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

       add.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment())
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
            // TODO после заполнения! и условие проверять по отфильтрованным данным
            // TODO ВНИМАТЕЛЬНО ТУТ УЖЕ ЕСТЬ ПЕРЕМЕННАЯ ГДЕ ХРАНЯТСЯ ОТФИЛЬТРОВАННЫЕ ШЕДУЛЫ
            if(this.schedules == null && schedules.isEmpty() /*&& schedule?.date == System.currentTimeMillis()*/)
                no_schedule_today.visibility = View.VISIBLE else View.GONE
            this.schedules = schedules
            updateSchedules()

        })
    }

    private fun updateSchedules() {
        if (schedules == null) return
        adapter.dataItems = schedules!!.filter { schedule ->
            when (pagePosition) { // TODO pagePosition явно лишний, убрать бы)
                0 -> isToday(schedule.date)
//                1 -> isSameWeek(schedule.date)
//                2 -> isMounth(schedule.date)
                else -> true
            }
        }
        adapter.notifyDataSetChanged()
    }

//    private fun updateSchedules() {
//        if (schedules == null) return
//        if (isToday(schedule!!.date))
//            adapter.dataItems = schedules!!
//        adapter.notifyDataSetChanged()
//    }


//    private fun observeSchedule() {
//        viewModel.getSchedules().observe(this, Observer { schedule ->
//            val task = schedule.firstOrNull { s -> s.id == id }
//            task?.checkBoxDone
//            adapter.dataItems = schedule
//            adapter.notifyDataSetChanged()
//
//        })
//    }
}
