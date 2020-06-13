package com.levkorol.todo.ui.schedule

import android.app.*
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.datepicker.MaterialDatePicker
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_add_schedule.add_title_text
import kotlinx.android.synthetic.main.fragment_add_schedule.back_profile
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE


class AddScheduleFragment : Fragment() {

    private lateinit var schedule: Schedule
    private var date: Long = 1
    private var time: Long = 1
    private var alarmFlag = false
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        date = currentTimeMillis()
        time = currentTimeMillis()
      //  date_selected.text = Tools.dateToString(date)
    }

    private fun initViews() {
        save_btn.setOnClickListener {
                saveSchedule()
                parentFragmentManager.popBackStack()
                //Toast.makeText(activity,"Добавлено в расписание", Toast.LENGTH_LONG).show()
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
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        add_time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(HOUR_OF_DAY, hour)
                cal.set(MINUTE, minute)
                time_tv.text = SimpleDateFormat("HH:mm").format(cal.time)
                time = cal.time.time
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(HOUR_OF_DAY),
                cal.get(MINUTE),
                true
            ).show()
        }

        if (alarmFlag) switch_.isChecked = true
        switch_.setOnClickListener {
            if (switch_.isChecked) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Включить оповещение?")
                builder.setPositiveButton("Да") { dialog, which ->
                    if (switch_.isChecked) alarmFlag = true
                }
                builder.setNegativeButton("Отмена") { dialog, which ->
                    switch_.isChecked = false
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                switch_.isChecked
            }
        }
    }

    private fun saveSchedule() {
        // TODO это нужно делать при сохранении (при добавлении шедула)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
//        val cal = Calendar.getInstance()
//        cal.timeInMillis = currentTimeMillis()
//        cal.set(HOUR_OF_DAY, 14)
//        cal.set(MINUTE, 42)
//        alarmManager?.set(
//            RTC_WAKEUP,
//            currentTimeMillis() + 60,
//            alarmIntent
//        )
//        alarmManager?.cancel(alarmIntent)

        if (alarmFlag) {
            alarmManager?.set(
                RTC_WAKEUP,
                currentTimeMillis() + 60,
                alarmIntent
            )
        }

            schedule = Schedule(
                title = add_title_text.text.toString(),
                description = add_description_text.text.toString(),
                date = date,
                checkBoxDone = false,
                time = time,
                alarm = alarmFlag
            )
            MainRepository.addSchedule(schedule)

    }
}
