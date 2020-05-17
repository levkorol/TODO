package com.levkorol.todo.ui.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter (fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ScheduleFragment()
            1 -> fragment = WeekFragment()
            2 -> fragment = MonthFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 3

//    override fun getPageTitle(position: Int): CharSequence? {
//        var title: String? = null
//        when (position) {
//            0 -> title = "Сегодня"
//            1 -> title = "Неделя"
//            2 -> title = "Месяц"
//        }
//        return title!!
//    }
}


