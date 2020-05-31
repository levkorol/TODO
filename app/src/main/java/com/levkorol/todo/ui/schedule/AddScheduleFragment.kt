package com.levkorol.todo.ui.schedule


import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker

import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.note.AddNoteFragment
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_add_schedule.add_title_text
import kotlinx.android.synthetic.main.fragment_add_schedule.back_profile

class AddScheduleFragment : Fragment() {

    private lateinit var schedule: Schedule

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        save_btn.setOnClickListener {
            saveSchedule()
            //Toast.makeText(activity,"Добавлено в расписание", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        add_date.setOnClickListener {
            //TODO как открыть виджет даты и сохранить выбранное значение
            val builder = MaterialDatePicker.Builder.datePicker()

            builder.build().show(parentFragmentManager, TAG)
        }

        add_time.setOnClickListener {
            //TODO как сделать и открыть виджет время и сохранить выбранное значение
        }

        switch_.setOnClickListener {
            //TODO как вкл оповещение на заданное время
        }
    }

    private fun saveSchedule() {
        schedule = Schedule(
            title = add_title_text.text.toString(),
            description = add_description_text.text.toString(),
            date = 1, //TODO как выставить дату из фрагмента календаря
            checkBoxDone = false,
            alarm = false //TODO как выставить время
        )
        MainRepository.addSchedule(schedule)
    }
}
