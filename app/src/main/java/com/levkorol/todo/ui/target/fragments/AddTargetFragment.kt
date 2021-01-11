package com.levkorol.todo.ui.target.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets
import com.levkorol.todo.utils.DEFAULT_DATE
import com.levkorol.todo.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add_target.*


class AddTargetFragment : Fragment() {
    private var date = DEFAULT_DATE
    private lateinit var targets: Targets
    private lateinit var backgroundTarget: Targets.BackgroundTarget
    private var imageCount: Int = 0

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
            hideKeyboard()
        }

        save_target_btn.setOnClickListener {
            if (name_target.text.isNotEmpty()) {
                hideKeyboard()
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
        if (white_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.WHITE
        if (grey_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.PURPLE
        if (darkblu_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.DARKER_BLU
        if (oranzh_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.ORANGE
        if (kiwi_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.KIWI
        if (blu_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.LIGHT_BLU
        if (pink_btn.isChecked) backgroundTarget = Targets.BackgroundTarget.PINK_LIGHT

        if (ic_1.isChecked) imageCount = 1
        if (ic_2.isChecked) imageCount = 2
        if (ic_3.isChecked) imageCount = 3
        if (ic_4.isChecked) imageCount = 4
        if (ic_5.isChecked) imageCount = 5
        if (ic_6.isChecked) imageCount = 6
        if (ic_7.isChecked) imageCount = 7
        if (ic_8.isChecked) imageCount = 8
        if (ic_9.isChecked) imageCount = 9
        if (ic_10.isChecked) imageCount = 10
        if (ic_11.isChecked) imageCount = 11
        if (ic_12.isChecked) imageCount = 12
        if (ic_13.isChecked) imageCount = 13
        if (ic_14.isChecked) imageCount = 14
        if (ic_15.isChecked) imageCount = 15
        if (ic_16.isChecked) imageCount = 16

        targets = Targets(
            name = name_target.text.toString(),
            description = disc_target.text.toString(),
            date = date,
            targetDone = false,
            startData = System.currentTimeMillis(),
            stopData = 0,
            days = 0,
            background = backgroundTarget,
            image = imageCount,
            inArchive = false,
            time = 0
        )
        MainRepository.addTargets(targets)
    }
}
