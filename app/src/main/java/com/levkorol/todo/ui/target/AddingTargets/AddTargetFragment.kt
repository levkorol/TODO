package com.levkorol.todo.ui.target.AddingTargets


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager

import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.target.TimerTarget
import com.levkorol.todo.utils.DEFAULT_DATE
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_add_target.*
import kotlinx.android.synthetic.main.fragment_add_target.name_target
import kotlinx.android.synthetic.main.item_target_list.*


class AddTargetFragment : Fragment() {
    private var date = DEFAULT_DATE
    private lateinit var targets: Targets

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_target, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date = System.currentTimeMillis()
        initViews()
    }

    private fun initViews() {
        back_click.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        save_target_btn.setOnClickListener {
            if (name_target.text.isNotEmpty()) {
                saveTarget()
                Toast.makeText(
                    activity,
                    "Отлично! Цель добавлена! Удачи в выполнении!",
                    Toast.LENGTH_LONG
                ).show()
                parentFragmentManager.popBackStack()

            } else {
                Toast.makeText(activity, "Введите название своей цели.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveTarget() {
        TimerTarget.start()
        targets = Targets(
            name = name_target.text.toString(),
            description = disc_target.text.toString(),
            date = date,
            targetDone = false,
            days = 0,
            time = 0,
            dateCreate = date
        )
        MainRepository.addTargets(targets)
    }
}
