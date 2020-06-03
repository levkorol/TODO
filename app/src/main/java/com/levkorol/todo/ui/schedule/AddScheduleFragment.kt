package com.levkorol.todo.ui.schedule

import android.app.TimePickerDialog
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_add_schedule.add_title_text
import kotlinx.android.synthetic.main.fragment_add_schedule.back_profile
import java.text.SimpleDateFormat
import java.util.*


class AddScheduleFragment : Fragment() {

    private lateinit var schedule: Schedule
    private var date: Long = 1
    private var time: Long = 1

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
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<Long> = builder.build()
            picker.addOnPositiveButtonClickListener { unixTime ->
                date_selected.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(unixTime))
                date = unixTime
//                date = if(date_selected.text != null) {
//                    unixTime
//                } else {
//                    System.currentTimeMillis() //todo в сегодняшнюю дату добавить schedule если дата не выбрана
//                }

            }
            picker.show(parentFragmentManager, picker.toString())
        }

        add_time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time_tv.text = SimpleDateFormat("HH:mm").format(cal.time)
                time = cal.time.time
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        switch_.setOnClickListener {
            //TODO  оповещение на заданное время
        }
    }

    private fun saveSchedule() {
        schedule = Schedule(
            title = add_title_text.text.toString(),
            description = add_description_text.text.toString(),
            date = date,
            checkBoxDone = false,
            time = time,
            alarm = false //TODO оповещение
        )
        MainRepository.addSchedule(schedule)
    }
}
