package com.levkorol.todo.ui.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.schedule.AlarmReceiver
import com.levkorol.todo.ui.schedule.ScheduleViewModel
import com.levkorol.todo.ui.setting.profile.AuthorizationFragment
import com.levkorol.todo.ui.setting.profile.ProfileFragment
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.setting_fragment.*
import java.util.*

class SettingFragment : Fragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var schedules: List<Schedule>? = null
    private var todayFlag = false
    private var id: Long = 1
    private lateinit var viewModelSchedule: ScheduleViewModel
    private var alarmFlag = false

    companion object {
        fun newInstance() = SettingFragment()
    }

   // private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO полночь
        // TODO получаем задачи
        // TODO проходимся по ним
        // TODO если время задачи меньше текущего и она не выполнена
        // TODO то тогда время задачи поменять день на текущий
        // TODO сохранить!

        //todo swich переноски таска на след день
        if (todayFlag) sw_schedule_up.isChecked = true
        sw_schedule_up.setOnClickListener {
            schedules?.filter {
                isToday(1)
            }
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

//        if (alarmFlag) off_notify.isChecked = true
//        off_notify.setOnClickListener {
//            if (off_notify.isChecked) {
//                val builder = MaterialAlertDialogBuilder(requireContext())
//                builder.setMessage("Выключить все оповещения?")
//                builder.setPositiveButton("Да") { _, _ ->
//                    if (off_notify.isChecked) alarmFlag = false
//                }
//                builder.setNegativeButton("Отмена") { _, _ ->
//                    off_notify.isChecked = true
//                }
//                val dialog: AlertDialog = builder.create()
//                dialog.show()
//            } else {
//                 off_notify.isChecked
//            }
//        }

        pin.setOnClickListener {
            pin.isChecked = false
            Toast.makeText(activity, "Установка пароля будет доступна в следующей версии", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModelSchedule = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSetting()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(SettingFragment())
    }

    private fun observeSetting() {
        viewModelSchedule.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
        })
    }
}
