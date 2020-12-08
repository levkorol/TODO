package com.levkorol.todo.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.utils.getColorCompat

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view.background == null) {
            view.background =
                ColorDrawable(view.context.getColorCompat(R.color.fragment_background))
        }
    }
}