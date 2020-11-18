package com.levkorol.todo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.levkorol.todo.R
import com.levkorol.todo.ui.notes.NotesFragment
import com.levkorol.todo.ui.schedule.ScheduleFragment
import com.levkorol.todo.ui.setting.SettingFragment
import com.levkorol.todo.ui.setting.on_boarding.HelperActivity
import com.levkorol.todo.ui.target.TargetFragment
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val MY_SETTINGS = "MY_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences(
            MY_SETTINGS,
            Context.MODE_PRIVATE
        )

        val hasVisited = sp.getBoolean("hasVisited", false)
        if (!hasVisited) {
            val intent = Intent(this, HelperActivity::class.java)
            startActivity(intent)
            val e = sp.edit()
            e.putBoolean("hasVisited", true)
            e.apply()
        } else {
            replaceFragment(ScheduleFragment(), false)
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


//        val permissions = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.MANAGE_DOCUMENTS)
//        for( permission in permissions){
//            if (ContextCompat.checkSelfPermission(this, permission)
//                != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        permission)) {
//                } else {
//                    ActivityCompat.requestPermissions(this,
//                        permissions, 0);
//                }
//            }
//        }
    }

//    fun weHavePermissionToReadContacts(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_CALENDAR
//        ) == PackageManager.PERMISSION_GRANTED
//    }


    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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
