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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.folder.AddFolderFragment
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : Fragment() {
    private var parentFolderId: Long = -1
    private lateinit var viewModel: ScheduleViewModel
    private var date: Long = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun initViews() {
        add_new_schedule.setOnClickListener {
             (activity as MainActivity).loadFragment(AddScheduleFragment())
        }

    }

    private fun showAlterDialogAdding() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Что вы хотите сделать?")
        val pictureDialogItems =
            arrayOf(
                "Добавить задачу",
                "Создать заметку",
                "Создать папку",
                "Добавить цель",
                "Все отменить и погладить льва"
            )
        builder.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> (activity as MainActivity).loadFragment(AddScheduleFragment())
                1 -> (activity as MainActivity).loadFragment(AddNoteFragment.newInstance(parentFolderId))
                2 -> (activity as MainActivity).loadFragment(AddFolderFragment.newInstance(parentFolderId))
                3 -> {
                }
                4 -> {
                }
            }
        }
        builder.show()
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TodayFragment()
            1 -> fragment = MonthFragment()
            2 -> fragment = AllScheduleListFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Сегодня"
            1 -> title = "Месяц"
            2 -> title = "Все время"
        }
        return title!!
    }
}
