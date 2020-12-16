package com.levkorol.todo


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.levkorol.todo.data.DataProvider
import com.levkorol.todo.ui.note.fragments.NotesFragment
import com.levkorol.todo.ui.schedule.fragments.ScheduleFragment
import com.levkorol.todo.ui.setting.SettingFragment
import com.levkorol.todo.ui.setting.on_boarding.HelperActivity
import com.levkorol.todo.ui.setting.pincode.PinCodFragment
import com.levkorol.todo.ui.target.fragments.TargetFragment
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val userRepo = DataProvider.userRepo

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (userRepo.needToRequestDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }

            if (!userRepo.hasVisited) {
                val intent = Intent(this, HelperActivity::class.java)
                startActivity(intent)
                userRepo.setVisited(true)
            } else if (userRepo.needToRequestPinCode) {
                replaceFragment(PinCodFragment())
            } else {
                replaceFragment(ScheduleFragment(), false)
            }
        } else if (!userRepo.needToRequestDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            if (!userRepo.hasVisited) {
                val intent = Intent(this, HelperActivity::class.java)
                startActivity(intent)
                userRepo.setVisited(true)
            } else if (userRepo.needToRequestPinCode) {
                replaceFragment(PinCodFragment())
            } else {
                replaceFragment(ScheduleFragment(), false)
            }
        }


        bottom_nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.item_schedule -> {
                    replaceFragment(ScheduleFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_note -> {
                    replaceFragment(NotesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_target -> {
                    replaceFragment(TargetFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_setting -> {
                    replaceFragment(SettingFragment())
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

    fun setBottomNavigationVisible(isVisible: Boolean) {
        if (isVisible) {
            bottom_nav_view.visibility = View.VISIBLE
        } else {
            bottom_nav_view.visibility = View.GONE
        }
    }
}
