package com.levkorol.todo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.schedule.ScheduleFragment
import com.levkorol.todo.ui.setting.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadFragment(NotesFragment())

        bottom_nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_note -> {
                    loadFragment(NotesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_schedule -> {
                    loadFragment(ScheduleFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_setting -> {
                    loadFragment(SettingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }

        }

    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun updateNavigation(fragment: Fragment) {
        if (fragment is NotesFragment) bottom_nav_view.menu.findItem(R.id.item_note)
            .isChecked = true
        if (fragment is ScheduleFragment) bottom_nav_view.menu.findItem(R.id.item_schedule)
            .isChecked = true
        if (fragment is SettingFragment) bottom_nav_view.menu.findItem(R.id.item_setting)
            .isChecked = true
    }


}
