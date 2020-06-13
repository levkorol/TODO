package com.levkorol.todo.ui.setting

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.schedule.ScheduleViewModel
import com.levkorol.todo.ui.setting.profile.AuthorizationFragment
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingFragment : Fragment() {

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

       sinhr.setOnClickListener {
           (activity as MainActivity).loadFragment(AuthorizationFragment())
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

    }

}
