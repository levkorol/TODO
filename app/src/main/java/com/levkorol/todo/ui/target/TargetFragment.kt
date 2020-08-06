package com.levkorol.todo.ui.target

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.target.AddingTargets.AddTargetFragment
import com.levkorol.todo.ui.target.viewpagertargets.CompletedFragment
import com.levkorol.todo.ui.target.viewpagertargets.MyHabitsFragment
import com.levkorol.todo.ui.target.viewpagertargets.MyTargetsFragment
import kotlinx.android.synthetic.main.target_fragment.*
import java.util.*

class TargetFragment : Fragment() {

    companion object {
        fun newInstance() = TargetFragment()
    }

    private lateinit var viewModel: TargetViewModel
    private var pagePosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.target_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerTarget.adapter = ViewPagerAdapterTarget(childFragmentManager)
        tabsTarget.setupWithViewPager(viewPagerTarget)
        viewPagerTarget.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                pagePosition = position
                //   update()
            }
        })

        add_.setOnClickListener {
            (activity as MainActivity).loadFragment(AddTargetFragment())
        }
 //       resources.getQuantityString(R.plurals.seconds, 13)  дни дней днях
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(TargetFragment())
    }


}

class ViewPagerAdapterTarget(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MyTargetsFragment()
            1 -> fragment = MyHabitsFragment()
           // 2 -> fragment = CompletedFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Мои цели"
            1 -> title = "Архив"
            //2 -> title = "Архив"
        }
        return title!!
    }

}
