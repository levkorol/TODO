package com.levkorol.todo.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel
  //  private lateinit var adapterToday: ScheduleAdapterToday
    private var id: Long = -1
    private var date: Long = 1

//    companion object {
//        private const val SCHEDULE_ID = "SCHEDULE_ID"
//        fun newInstance(id: Long): ScheduleFragment {
//            val fragment = ScheduleFragment()
//            val arguments = Bundle()
//            arguments.apply {
//                putLong(SCHEDULE_ID, id)
//            }
//            fragment.arguments = arguments
//            return fragment
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (arguments != null) {
//            id = arguments?.getLong(SCHEDULE_ID, id)!!
//        }
//
//        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_sch)
//        val llm = LinearLayoutManager(view.context)
//        llm.orientation = LinearLayoutManager.VERTICAL
//        adapterToday = ScheduleAdapterToday(activity as MainActivity)
//        recyclerView.layoutManager = llm
//        recyclerView.adapter = adapterToday

        initViews()

        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        tabs.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                pagePosition = position
             //   updateSchedules()
            }
        })

        date = System.currentTimeMillis()
        dateToday.text = Tools.dateToString(date)

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
           // updateSchedules()
        })
    }

//    private fun updateSchedules() {
//        if (schedules == null) return
//        adapterToday.dataItems = schedules!!.filter { schedule ->
//            when (pagePosition) {
//                0 -> isToday(schedule.date)
//              //  1 -> isSameWeek(schedule.date)
//                1 -> isMounth(schedule.date)
//                else -> true
//            }
//        }
//        adapterToday.notifyDataSetChanged()
//    }

    private fun initViews() {
        add_new_schedule.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment())
        }

        list_all_schedule.setOnClickListener {
            (activity as MainActivity).loadFragment(AllScheduleListFragment())
        }
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TodayFragment()
            1 -> fragment = MonthFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Сегодня"
            1 -> title = "Месяц"
        }
        return title!!
    }
}
