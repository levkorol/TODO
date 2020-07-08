package com.levkorol.todo.ui.setting.on_boarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.activity.*





class HelperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        view_pager.adapter = ViewPagerAdapter(supportFragmentManager)

        next.setOnClickListener {
            // TODO setPage + viewPager.currentItem
            for (i in 0..3) {
                setPage(i + 1)
                if (i == 3) {
                    val intent =  Intent(this, MainActivity::class.java)
                    startActivity(intent) // TODO finish мб
                }
            }
        }
    }

    fun setPage(page: Int) {
        view_pager.setCurrentItem(page, true)
    }

    fun loadFragmentActivity(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerActivity, fragment)
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.slide_out_right
            )
            .addToBackStack(null)
            .commit()
    }
}

  class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =  OnBoardingFragment()
            1 -> fragment =  OnBoarding2Fragment()
            2 -> fragment =  OnBoarding3Fragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 3

 }