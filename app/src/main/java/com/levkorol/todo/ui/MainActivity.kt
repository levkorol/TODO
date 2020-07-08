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
import android.content.Context
import com.levkorol.todo.ui.setting.on_boarding.HelperActivity


class MainActivity : AppCompatActivity() {
    private val  MY_SETTINGS = "MY_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences(
            MY_SETTINGS,
            Context.MODE_PRIVATE
        )

        val hasVisited = sp.getBoolean("hasVisited", false)

        if (!hasVisited) {

           // loadFragment(OnBoardingFragment())
            val intent =  Intent(this, HelperActivity::class.java)
            startActivity(intent)

            val e = sp.edit()
            e.putBoolean("hasVisited", true)
            e.apply()
        } else {

            loadFragment(NotesFragment())
        }

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // TODO открываем нужный фрагмент
        //if (intent?.hasExtra("IS_NOTE") == true)
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.slide_out_right
            )
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
