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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.note.NoteFragment
import com.levkorol.todo.utils.Tools
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_add_schedule.back_profile
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE


class AddScheduleFragment : Fragment() {

    private lateinit var schedule: Schedule
    private var date: Long = 1
    private var dateAdd: Long = 1
    private var time: Long = 1
    private var alarmFlag = false
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    companion object {
        private const val DATE = "DATE"

        fun newInstance(date: Long): AddScheduleFragment {
            val fragment = AddScheduleFragment()
            val arg = Bundle()
            arg.apply {
                putLong(DATE, date)
            }
            fragment.arguments = arg
            return fragment
        }
    }

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

          date_selected.text = Tools.dateToString(date)
        if (arguments != null) {
            dateAdd = arguments?.getLong(DATE, -1)!!
            date_selected.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(dateAdd))
            date = dateAdd
        }
    }

    private fun initViews() {
        save_btn.setOnClickListener {
            if (add_description_text.text.isNotEmpty()) {
                saveSchedule()
                Toast.makeText(activity, "Добавлено в расписание", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()

            } else {
                Toast.makeText(activity, "Введите описание задачи", Toast.LENGTH_LONG).show()
            }
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
                clear_time.visibility = View.VISIBLE
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
            if (time == 1.toLong()) {
                Toast.makeText(activity, "Выставьте время выполнения", Toast.LENGTH_LONG).show()
                alarmFlag = false
                switch_.isChecked = false
            } else {
                alarmFlag = true
            }
        }

        clear_time.setOnClickListener {
            time = 1
            time_tv.text = "Назначить время выполнения"
            clear_time.visibility = View.GONE
        }
    }

    private fun saveSchedule() {
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        if (alarmFlag) {
            alarmManager?.set(
                RTC_WAKEUP,
                currentTimeMillis() + 60,
                alarmIntent
            )
        }

        schedule = Schedule(
            description = add_description_text.text.toString(),
            date = date,
            checkBoxDone = false,
            time = time,
            alarm = alarmFlag
        )
        MainRepository.addSchedule(schedule)

    }
}
