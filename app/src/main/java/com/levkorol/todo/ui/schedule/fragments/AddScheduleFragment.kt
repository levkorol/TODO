package com.levkorol.todo.ui.schedule.fragments

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.schedule.AlarmReceiver
import com.levkorol.todo.utils.DateUtil
import com.levkorol.todo.utils.Tools
import com.levkorol.todo.utils.mergeDateHoursMinutes
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class AddScheduleFragment : Fragment() {

    private lateinit var schedule: Schedule
    private val dates = mutableListOf<Date>()
    private var date: Long = 1
    private var dateAdd: Long = 1
    private var hours: Int = -1
    private var minutes: Int = -1
    private var alarmFlag = false
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var scheduleId: Long = 0
    private var addTime = false
    private var repeatAfter7day = true

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
                saveSchedule(date)
                //         saveRepeatingSchedule(dates)
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
                setupRepeatingTask(date)
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        add_time.setOnClickListener {
            val cal = Calendar.getInstance()
            // cal.timeInMillis = 0
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(HOUR_OF_DAY, hour)
                cal.set(MINUTE, minute)
                cal.set(SECOND, 0)
                cal.set(MILLISECOND, 0)
                time_tv.text = SimpleDateFormat("HH:mm").format(cal.time)
                hours = hour
                minutes = minute
                //time = cal.time.time
                addTime = true
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
            if (hours == -1 || minutes == -1) {
                Toast.makeText(activity, "Назначьте время выполнения", Toast.LENGTH_LONG).show()
                alarmFlag = false
                switch_.isChecked = false
            } else {
                alarmFlag = true
            }
        }

        clear_time.setOnClickListener {
            //time = 1
            addTime = false
            time_tv.text = "Назначить время выполнения"
            clear_time.visibility = View.GONE
        }
    }

    // TODO Разобраться с логикой повторения
    private fun setupRepeatingTask(date: Long) {
        val originalDate = Date(date)
        if (repeatAfter7day) {
            val dateAfter7Days = DateUtil.addDays(originalDate, 7)
            dates.add(dateAfter7Days)
        }
        dates.add(originalDate)
    }

    private fun saveRepeatingSchedule(dates: List<Date>) {
        dates.forEach {
            saveSchedule(it.time)
        }
    }

    private fun saveSchedule(date: Long) {
        schedule = Schedule(
            description = add_description_text.text.toString(),
            comment = "",
            date = date,
            checkBoxDone = false,
            hours = hours,
            minutes = minutes,
            alarm = alarmFlag,
            addTime = addTime,
            archive = false
        )
        MainRepository.addSchedule(schedule) { id ->
            scheduleId = id
            setAlarm()
        }
    }

    private fun setAlarm() {
        if (alarmFlag) {

            alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra("SCHEDULE_ID", scheduleId)
                intent.putExtra("NOTE", false)
                Log.i("AddScheduleFragment", "saveScheduleExtras${intent.extras}")
                PendingIntent.getBroadcast(context, 0, intent, FLAG_CANCEL_CURRENT)
            }

            val needTime = mergeDateHoursMinutes(date, hours, minutes)
            alarmManager?.set(
                RTC_WAKEUP,
                needTime,
                alarmIntent
            )
            Log.i("AddScheduleFragment", "need $needTime ,date = $date, time = ")
        }
    }
}
