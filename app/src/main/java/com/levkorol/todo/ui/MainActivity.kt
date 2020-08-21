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
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.levkorol.todo.ui.note.NoteFragment
import com.levkorol.todo.ui.schedule.TodayFragment
import com.levkorol.todo.ui.setting.on_boarding.HelperActivity
import com.levkorol.todo.ui.target.TargetFragment
import com.levkorol.todo.utils.getMillisecondsWithoutCurrentTime


class MainActivity : AppCompatActivity() {
    private val MY_SETTINGS = "MY_SETTINGS"
    private var noteId: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("MainActivity", "Date ${getMillisecondsWithoutCurrentTime(1595967790000)}")

        val sp = getSharedPreferences(
            MY_SETTINGS,
            Context.MODE_PRIVATE
        )

        val hasVisited = sp.getBoolean("hasVisited", false)

        if (!hasVisited) {

            // loadFragment(OnBoardingFragment())
            val intent = Intent(this, HelperActivity::class.java)
            startActivity(intent)

            val e = sp.edit()
            e.putBoolean("hasVisited", true)
            e.apply()
        } else {
            loadFragment(ScheduleFragment())
        }

        bottom_nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.item_schedule -> {
                    loadFragment(ScheduleFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_note -> {
                    loadFragment(NotesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_target -> {
                    loadFragment(TargetFragment())
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
//        if (intent?.hasExtra("IS_NOTE") == true) { //todo
//            noteId = intent.getLongExtra("ID", noteId)
//            loadFragment(NoteFragment.instance(noteId))
//
//        } else {
//            intent?.getBooleanExtra("IS_NOTE", false)
            loadFragment(ScheduleFragment())
      //  }
        Log.i("MainActivity", "id ${noteId}")
        Log.i("MainActivity", "intent ${intent?.extras}")
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            .setCustomAnimations(
//                android.R.anim.slide_in_left,
//                android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.slide_out_right
//            )
            .addToBackStack(null)
            .commit()
    }

    fun updateNavigation(fragment: Fragment) {
        if (fragment is NotesFragment) bottom_nav_view.menu.findItem(R.id.item_note)
            .isChecked = true
        if (fragment is ScheduleFragment) bottom_nav_view.menu.findItem(R.id.item_schedule)
            .isChecked = true
        if (fragment is TargetFragment) bottom_nav_view.menu.findItem(R.id.item_target)
            .isChecked = true
        if (fragment is SettingFragment) bottom_nav_view.menu.findItem(R.id.item_setting)
            .isChecked = true
    }
}
