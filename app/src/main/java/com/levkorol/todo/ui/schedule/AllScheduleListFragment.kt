package com.levkorol.todo.ui.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.utils.*
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.add
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.item_list_all_schedule.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class AllScheduleListFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var adapterAllSchedule: AllScheduleAdapter
    private var schedules: List<Schedule>? = null
    private var selectDate: Long = DEFAULT_DATE
    private var alarmFlag = false
    private var addTime = false

    companion object {
        private const val DATE = "DATE"

        fun newInstance(date: Long): AllScheduleListFragment {
            val fragment = AllScheduleListFragment()
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
        return inflater.inflate(R.layout.fragment_all_schedule_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_all_schedule)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterAllSchedule = AllScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterAllSchedule

        // TODO смотреть на selectDate
        if (arguments != null) {
            selectDate = arguments?.getLong(DATE, DEFAULT_DATE)!!
        }
//        selected_date.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(selectDate))
//        search_ll.visibility = View.VISIBLE
//        updateSchedules()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateNavigation(ScheduleFragment())
    }

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedules ->
            this.schedules = schedules
            updateSchedules()
        })
    }

    private fun updateSchedules() {
        if (schedules == null) return

        if (selectDate > 1) {
            adapterAllSchedule.dataItems = schedules!!.filter { element ->
                areDatesEqual(element.date, selectDate)
            }
        } else {
            adapterAllSchedule.dataItems = schedules!!.sortedByDescending { it.dateCreate }
        }

        if (adapterAllSchedule.dataItems.isEmpty()) {
            no_schedule.text = "Задач пока нет..."
        }

        adapterAllSchedule.notifyDataSetChanged()


    }

    private fun initViews() {
        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        filter_date.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker: MaterialDatePicker<Long> = builder.build()
            picker.addOnPositiveButtonClickListener { unixTime ->
                search_ll.visibility = View.VISIBLE
                selected_date.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(unixTime))
                selectDate = unixTime
                updateSchedules()
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        cancel_filter.setOnClickListener {
            selectDate = 1
            search_ll.visibility = View.GONE
            updateSchedules()
        }

        add.setOnClickListener {
            (activity as MainActivity).loadFragment(AddScheduleFragment.newInstance(selectDate))
        }
    }


    //ADAPTER
    inner class AllScheduleAdapter(
        val activity: MainActivity
    ) : RecyclerView.Adapter<AllScheduleAdapter.ScheduleViewHolder>() {
        private lateinit var schedule: Schedule
        private var alarmManager: AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent

        var dataItems: List<Schedule> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount() = dataItems.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(
                R.layout.item_list_all_schedule,
                parent, false
            )
            return ScheduleViewHolder(view)
        }

        override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
            val item = dataItems[position]
            var date: Long
            var time: Long

            holder.clearTime.setOnClickListener {
                item.addTime = false
                item.alarm = false
                holder.timeInEdit.text = "Не выставлено"
                holder.clearTime.visibility = View.GONE
                item.addTime = false
                schedule = dataItems[position]
                schedule.addTime = false
                MainRepository.updateSchedule(schedule)
            }

            holder.title_schedule.text = item.description
            holder.date_schedule.text = Tools.dateToString(item.date)

            if (!item.addTime ) {
                holder.timeInEdit.text = "Не выставлено"
            } else {
                holder.timeInEdit.text = Tools.convertLongToTimeString(item.hours, item.minutes)
                holder.clearTime.visibility = View.VISIBLE
            }

            holder.editTaskBtn.setOnClickListener {
                holder.saveText.visibility = View.VISIBLE
                holder.exitEditText.visibility = View.VISIBLE
                holder.editTaskBtn.text = "Редактирование текста:"
                with(holder.title_schedule) {
                    isFocusable = true
                    isFocusableInTouchMode = true
                    isLongClickable = true
                    isCursorVisible = true
                    isEnabled = true
                }
            }

            holder.exitEditText.setOnClickListener {
                notifyDataSetChanged()
                holder.editTaskBtn.text = "Изменить текст"
                holder.saveText.visibility = View.GONE
                holder.exitEditText.visibility = View.GONE
                with(holder.title_schedule) {
                    isFocusable = false
                    isFocusableInTouchMode = false
                    isLongClickable = false
                    isCursorVisible = false
                    isEnabled = false
                }
            }

            holder.saveText.setOnClickListener {
                holder.editTaskBtn.text = "Изменить текст"
                holder.saveText.visibility = View.GONE
                holder.exitEditText.visibility = View.GONE
                with(holder.title_schedule) {
                    isFocusable = false
                    isFocusableInTouchMode = false
                    isLongClickable = false
                    isCursorVisible = false
                    isEnabled = false
                }
                schedule = dataItems[position]
                schedule.description = holder.title_schedule.text.toString()
                MainRepository.updateSchedule(schedule)
                notifyDataSetChanged()
            }

            holder.swich.isChecked = item.alarm
            holder.swich.setOnClickListener {
                if(!item.addTime) {
                    Toast.makeText(activity, "Назначьте время выполнения", Toast.LENGTH_LONG).show()
                    holder.swich.isChecked = false
                }
                if(item.addTime) {
                    schedule = dataItems[position]
                    schedule.alarm = true
                    MainRepository.updateSchedule(schedule)
                    holder.swich.isChecked = true

                    alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("SCHEDULE_ID", item.id)
                        intent.putExtra("NOTE", false)
                        PendingIntent.getBroadcast(context, 0, intent, 0)
                    }
                    if (alarmFlag) {
                        alarmManager?.set(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + 60,
                            alarmIntent
                        )
                    }
                } //todo оповещения и свич
            }

            holder.dateInEdit.text = Tools.dateToString(item.date)

            holder.deleteTask.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(activity)
                builder.setMessage("Удалить: ${holder.title_schedule.text} ?")
                builder.setPositiveButton("Да") { _, _ ->
                    MainRepository.deleteSchedule(item.id)
                    //holder.editList.visibility = View.GONE
                    holder.editTaskBtn.text = "Изменить текст"
                    holder.saveText.visibility = View.GONE
                    holder.exitEditText.visibility = View.GONE
                    with(holder.title_schedule) {
                        isFocusable = false
                        isFocusableInTouchMode = false
                        isLongClickable = false
                        isCursorVisible = false
                        isEnabled = false
                    }
                }
                builder.setNegativeButton("Отмена") { _, _ ->
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }


            holder.changeDate.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                val picker: MaterialDatePicker<Long> = builder.build()
                picker.addOnPositiveButtonClickListener { unixTime ->
                    holder.dateInEdit.text =
                        SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(unixTime))
                    date = unixTime
                    schedule = dataItems[position]
                    schedule.date = date
                    schedule.checkBoxDone = false
                   MainRepository.updateSchedule(schedule)
                }
                picker.show(parentFragmentManager, picker.toString())
            }


            holder.changeTime.setOnClickListener {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    holder.timeInEdit.text = SimpleDateFormat("HH:mm").format(cal.time)
                    time = cal.time.time
                    addTime = true
                    holder.clearTime.visibility = View.VISIBLE
                    schedule = dataItems[position]
                    schedule.hours = hour
                    schedule.minutes = minute
                    schedule.addTime = true
                    MainRepository.updateSchedule(schedule)
                }
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }

        inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var date_schedule: TextView = itemView.findViewById(R.id.date)
            var title_schedule: TextView = itemView.findViewById(R.id.tv_title)
            var editTaskBtn: TextView = itemView.findViewById(R.id.edit_task)
            var deleteTask: TextView = itemView.findViewById(R.id.delete_task)
            var dateInEdit: TextView = itemView.findViewById(R.id.date_in_edit)
            var timeInEdit: TextView = itemView.findViewById(R.id.time_in_edit)
            var changeDate: LinearLayout = itemView.findViewById(R.id.edit_date)
            var changeTime: LinearLayout = itemView.findViewById(R.id.edit_time)
            var clearTime: TextView = itemView.findViewById(R.id.clear_time_in_edit)

            var saveText: TextView = itemView.findViewById(R.id.save_text)
            var exitEditText: TextView = itemView.findViewById(R.id.exit_edit_text)
            var swich: Switch = itemView.findViewById(R.id.swich_alarm)
        }
    }
}


//        holder.timer.setOnClickListener {
//            schedule = dataItems[position]
//            val builder = MaterialAlertDialogBuilder(activity)
//            builder.setMessage("Выключить оповещение на ${holder.time.text}?")
//            builder.setPositiveButton("Да") { _, _ ->
//                schedule.alarm = false
//                MainRepository.updateSchedule(schedule)
//            }
//            builder.setNegativeButton("Отмена") { _, _ ->
//            }
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
//        }