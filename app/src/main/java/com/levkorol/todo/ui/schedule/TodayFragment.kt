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
import com.levkorol.todo.ui.schedule.adapters.ScheduleAdapterToday
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : Fragment() {

    private lateinit var adapterToday: ScheduleAdapterToday
    private lateinit var viewModel: ScheduleViewModel
    private var schedules: List<Schedule>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_today)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterToday = ScheduleAdapterToday(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterToday

       add.setOnClickListener {
           (activity as MainActivity).loadFragment(AddScheduleFragment())
       }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
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
        adapterToday.dataItems = schedules!!.filter { schedule ->
            isToday(schedule.date)
        }.sortedWith(compareBy({it.checkBoxDone}, {it.time}))
        if(adapterToday.dataItems.isEmpty() ) {
            no_schedule_today.visibility = View.VISIBLE
        } else {
            no_schedule_today.visibility = View.GONE
        }
        adapterToday.notifyDataSetChanged()
    }
}
