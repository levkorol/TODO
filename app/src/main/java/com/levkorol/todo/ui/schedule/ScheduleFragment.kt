package com.levkorol.todo.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.schedule_fragment.*


class ScheduleFragment : Fragment() {

    internal lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var schedule: Schedule
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var adapter: ScheduleAdapter
    private var id : Long = -1

    companion object {
        private const val SCHEDULE_ID = "SCHEDULE_ID"
        fun newInstance(id: Long) : ScheduleFragment {
            val fragment = ScheduleFragment()
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
        return inflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null) {
            id = arguments?.getLong(SCHEDULE_ID, id)!!
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_sch)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = ScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

        initViews()

//TODO как сделать вью пейджер и переключение трех фрагментов(Сегодня Неделя Месяц во фрагменте Расписание

        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        tabs.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                pagePosition = position
                updateSchedules()
            }
        })
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

    private var pagePosition: Int = 0
    private var schedules: List<Schedule>? = null

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
            updateSchedules()
        })
    }

    private fun updateSchedules() {
        if (schedules == null) return
        adapter.dataItems =  schedules!!.filter { schedule ->
//            when (pagePosition) {
//                0 -> isToday(schedule.date)
//                1 -> isToday(schedule.date)
//                2 -> isToday(schedule.date)
//                else -> true
//            }
            true
        }
        adapter.notifyDataSetChanged()
    }

    private fun initViews() {
        add_new_schedule.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment())
        }
    }
}





class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
//            0 -> fragment = ScheduleFragment()
            0, 1 -> fragment = WeekFragment()
            2 -> fragment = MonthFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Сегодня"
            1 -> title = "Неделя"
            2 -> title = "Месяц"
        }
        return title!!
    }
}
