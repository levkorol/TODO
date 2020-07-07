package com.levkorol.todo.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.schedule.ScheduleViewModel
import com.levkorol.todo.ui.setting.profile.AuthorizationFragment
import com.levkorol.todo.ui.setting.profile.ProfileFragment
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var valueSchedule = MainRepository.getSchedules().value
    private var schedules: List<Schedule>? = null
    private var todayFlag = false
    private var id: Long = 1
    private lateinit var viewModelSchedule: ScheduleViewModel

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //todo swich переноски таска на след день
        if (todayFlag) sw_schedule_up.isChecked = true
        sw_schedule_up.setOnClickListener {
//             if () {
//                todayFlag = false
//                sw_schedule_up.isChecked = false
//            } else {
//                todayFlag = true
//            }
        }


        sinhr.setOnClickListener {
            if (auth.currentUser == null) {
                (activity as MainActivity).loadFragment(AuthorizationFragment())
            } else {
                (activity as MainActivity).loadFragment(ProfileFragment())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        observeSetting()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(SettingFragment())
    }

    private fun observeSetting() {
//        viewModelSchedule.getSchedules().observe(this, Observer { schedules ->
//            this.schedules = schedules.firstOrNull { s -> s.id == id }
//        })
    }

}
