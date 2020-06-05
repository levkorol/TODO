package com.levkorol.todo.ui.schedule


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity

class TodayFragment : Fragment() {

    private lateinit var adapter: ScheduleAdapter
    private var schedule: Schedule? = null
    private lateinit var viewModel: ScheduleViewModel
    private var id: Long = -1
    private var time: Long = -1

    companion object {
        private const val SCHEDULE_ID = "SCHEDULE_ID"
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


    }

    override fun onStart() {
        super.onStart()
        viewModel =
            ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()
    }

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedule ->
            val task = schedule.firstOrNull { s -> s.id == id }
            task?.checkBoxDone
            adapter.dataItems = schedule
            adapter.notifyDataSetChanged()
        })
    }

}
