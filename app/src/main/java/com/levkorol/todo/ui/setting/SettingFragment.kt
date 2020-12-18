package com.levkorol.todo.ui.setting


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.base.BaseFragment
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.schedule.viewmodel.ScheduleViewModel
import com.levkorol.todo.ui.setting.pincode.AddPinFragment
import com.levkorol.todo.ui.setting.profile.fragments.AuthorizationFragment
import com.levkorol.todo.ui.setting.profile.fragments.ProfileFragment
import com.levkorol.todo.utils.replaceFragment
import com.levkorol.todo.utils.showToastf
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingFragment : BaseFragment() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var schedules: List<Schedule>? = null
    private var todayFlag = false
    private var id: Long = 1
    private lateinit var viewModelSchedule: ScheduleViewModel
    private var alarmFlag = false
    private val userRepo = DataProvider.userRepo

    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sinhr.setOnClickListener {
            if (auth.currentUser == null) {
                replaceFragment(AuthorizationFragment())
            } else {
                replaceFragment(ProfileFragment())
            }
        }

        btn_web.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.levkorol.todo")
            );
            startActivity(browserIntent)
        }

        theme.isChecked = userRepo.needToRequestDarkTheme
        theme.setOnClickListener {
            if (!userRepo.needToRequestDarkTheme) {
                userRepo.needToRequestDarkTheme = true
                saveToDb(true)
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            } else {
                userRepo.needToRequestDarkTheme = false
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

//            AppCompatDelegate.setDefaultNightMode(
//                if (requireContext().isDarkThemeOn()) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
//            )
        }


        pin.setOnClickListener {
            replaceFragment(AddPinFragment())
        }

        lion_pick.setOnClickListener {
            Toast.makeText(
                activity,
                "Привет! Я Лев ",
                Toast.LENGTH_LONG
            ).show()
        }

        vidget.setOnClickListener {
            showToastf(requireContext(), "Виджет будет доступен в следующей версии:)")
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

    private fun saveToDb(isTheme: Boolean) {
        userRepo.setDarkTheme(isTheme)
    }
}
