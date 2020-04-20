package com.levkorol.todo.data

import androidx.fragment.app.Fragment

interface IFragmentRepository {
    fun switchFragment(fragment: Fragment)
}
