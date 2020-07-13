package com.levkorol.todo.ui.schedule

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.levkorol.todo.utils.Tools
import com.levkorol.todo.utils.isMounth
import com.levkorol.todo.utils.isToday
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.*
import kotlinx.android.synthetic.main.fragment_all_schedule_list.add
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class AllScheduleListFragment : Fragment() {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var adapterAllSchedule: AllScheduleAdapter
    private var schedules: List<Schedule>? = null
    private var selectDate: Long = 1
   // private var dateAdd: Long = 1
    private var date: Long = 1

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

        if (arguments != null) {
            selectDate = arguments?.getLong(DATE, -1)!!
            selected_date.text = SimpleDateFormat("EEEE, dd MMM, yyyy").format(Date(selectDate))
            search_ll.visibility = View.VISIBLE
            updateSchedules()
        }
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
        adapterAllSchedule.dataItems = schedules!!.sortedByDescending { it.dateCreate }
        if (adapterAllSchedule.dataItems.isEmpty()) {
            no_schedule.text = "Задач пока нет..."
        }
        if(selectDate > 1 ) {
            adapterAllSchedule.dataItems = schedules!!.filter { element ->
                element.date == selectDate
            }
        } else {
            adapterAllSchedule.dataItems = schedules!!
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

    inner class AllScheduleAdapter(
        val activity: MainActivity
    ) : RecyclerView.Adapter<AllScheduleAdapter.ScheduleViewHolder>() {

        private lateinit var schedule: Schedule
        private var isEditMode = false
        private var date: Long = 1
        private var time: Long = 1

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

            holder.title_schedule.text = item.description
            holder.date_schedule.text = Tools.dateToString(item.date)
            if (item.time == 1.toLong()) {
                holder.time.visibility = View.GONE
            } else {
                holder.time.text = Tools.convertLongToTimeString(item.time)
            }

            holder.timer.visibility =
                if (item.alarm && item.date < System.currentTimeMillis()
                    && item.time < System.currentTimeMillis()
                ) View.VISIBLE else View.GONE

            holder.editTaskBtn.setOnClickListener {
                holder.editList.visibility = View.VISIBLE
                holder.editTaskBtn.text = "Режим редактирования"
                isEditMode = true
                if (isEditMode) {
                    with(holder.title_schedule) {
                        isFocusable = true
                        isFocusableInTouchMode = true
                        isLongClickable = true
                        isCursorVisible = true
                        holder.title_schedule.isEnabled = true
                    }
                }
            }

            holder.exitEdit.setOnClickListener {
                notifyDataSetChanged()
                holder.editList.visibility = View.GONE
                holder.editTaskBtn.text = "Редактировать"
                isEditMode = false
                if (!isEditMode) {
                    holder.title_schedule.isFocusable = false
                    holder.title_schedule.isFocusableInTouchMode = false
                    holder.title_schedule.isLongClickable = false
                    holder.title_schedule.isCursorVisible = false
                    holder.title_schedule.isEnabled = false
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

            holder.saveTask.setOnClickListener {
                holder.editList.visibility = View.GONE
                holder.editTaskBtn.text = "Редактировать"
                isEditMode = false
                if (!isEditMode) {
                    holder.title_schedule.isFocusable = false
                    holder.title_schedule.isFocusableInTouchMode = false
                    holder.title_schedule.isLongClickable = false
                    holder.title_schedule.isCursorVisible = false
                    holder.title_schedule.isEnabled = false
                }
                schedule = dataItems[position]

                schedule.description = holder.title_schedule.text.toString()
                schedule.date = date
                schedule.time = time
               // schedule.alarm
                // schedule.dateCreate = System.currentTimeMillis()

                MainRepository.updateSchedule(schedule)

                notifyDataSetChanged()
            }

            holder.dateInEdit.text = Tools.dateToString(item.date)
            holder.timeInEdit.text = Tools.convertLongToTimeString(item.time)


            holder.deleteTask.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(activity)
                builder.setMessage("Удалить: ${holder.title_schedule.text} ?")
                builder.setPositiveButton("Да") { _, _ ->
                    MainRepository.deleteSchedule(item.id)
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
                  //  clear_time_edit.visibility = View.VISIBLE
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
            var time: TextView = itemView.findViewById(R.id.tv_time)
            var title_schedule: TextView = itemView.findViewById(R.id.tv_title)
            var timer: ImageView = itemView.findViewById(R.id.iv_notify)
            var editTaskBtn: TextView = itemView.findViewById(R.id.edit_task)
            var editList: LinearLayout = itemView.findViewById(R.id.edit_ll)
            var exitEdit: TextView = itemView.findViewById(R.id.exit_edit)
            var deleteTask: TextView = itemView.findViewById(R.id.delete_task)
            var saveTask: TextView = itemView.findViewById(R.id.save_task)
            var dateInEdit: TextView = itemView.findViewById(R.id.date_in_edit)
            var timeInEdit: TextView = itemView.findViewById(R.id.time_in_edit)
            var changeDate: LinearLayout = itemView.findViewById(R.id.edit_date)
            var changeTime: LinearLayout = itemView.findViewById(R.id.edit_time)
        }
    }
}
