package com.levkorol.todo.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.folder.AddFolderFragment
import kotlinx.android.synthetic.main.schedule_fragment.*


class ScheduleFragment : Fragment() {

    internal lateinit var viewPagerAdapter: ViewPagerAdapter

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.levkorol.todo.R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

//        val mViewPager = view.findViewById(R.id.viewPager) as ViewPager
//        val tableView = view.findViewById(R.id.tabs) as TabLayout
//        tableView.setupWithViewPager(mViewPager)
//        mViewPager.adapter = ViewPagerAdapter(childFragmentManager)

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

    }

    private fun initViews() {
        add_new_schedule.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment())
        }
    }
}


