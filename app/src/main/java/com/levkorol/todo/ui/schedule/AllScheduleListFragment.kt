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
import com.google.android.material.datepicker.MaterialDatePicker

import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.schedule.adapters.AllScheduleAdapter
import com.levkorol.todo.utils.isMounth
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.add
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class AllScheduleListFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var adapterAllSchedule: AllScheduleAdapter
    private var schedules: List<Schedule>? = null
    private var selectDate: Long = 1
    private var dateAdd: Long = 1
    private var date: Long = 1

    companion object {
        private const val DATE = "DATE"

        fun newInstance(date: Long): AllScheduleListFragment {
            val fragment = AllScheduleListFragment()
            val arg = Bundle()
            arg.apply {
                putLong(DATE, date)
            }
            fragment.arguments = arg
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            dateAdd = arguments?.getLong(DATE, -1)!!
            selected_date.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(dateAdd))
            dateAdd = date
            adapterAllSchedule.dataItems = schedules!!.sortedByDescending { it.date }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_all_schedule)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterAllSchedule = AllScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterAllSchedule

        initViews()

    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(ScheduleFragment())
    }

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
            updateSchedules()
        })
    }

    private fun updateSchedules() {
        if (schedules == null) return
        adapterAllSchedule.dataItems = schedules!!.sortedByDescending { it.dateCreate }
        if(adapterAllSchedule.dataItems.isEmpty() ) {
            no_schedule.text = "Задач пока нет..."
        }
        adapterAllSchedule.notifyDataSetChanged()

    }

    private fun initViews() {
        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        filter_date.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<Long> = builder.build()
            picker.addOnPositiveButtonClickListener { unixTime ->
                search_ll.visibility = View.VISIBLE
                selected_date.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(unixTime))
                selectDate = unixTime
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        cancel_filter.setOnClickListener {
            selectDate = 1
            search_ll.visibility = View.GONE
        }

        add.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment.newInstance(selectDate))
        }
    }
}
