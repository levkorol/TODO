package com.levkorol.todo.ui.schedule

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import androidx.recyclerview.widget.DiffUtil
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.folder.AddFolderFragment
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.utils.Tools


class ScheduleAdapter(
    val activity: MainActivity
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    // private val context: Context? = null
    private lateinit var schedule: Schedule

    var dataItems: List<Schedule> = listOf()
        set(value) {
//            val diffCallback = DiffCallback(field, value)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            diffResult.dispatchUpdatesTo(this)
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = dataItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.item_list_schedule,
            parent, false
        )
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = dataItems[position]
        holder.title_schedule.text = item.title
//        if(item.date == System.currentTimeMillis()){
//            holder.date_schedule.visibility = View.GONE
//        } else {
//
//        }
//        if (item.time == System.currentTimeMillis()) {
//            holder.time.visibility = View.GONE
//        } else {
//            holder.time.visibility = View.VISIBLE
//        }
        holder.date_schedule.text = Tools.dateToString(item.date)
        holder.time.text = Tools.convertLongToTimeString(item.time)
        holder.checkBox.isChecked = item.checkBoxDone
        holder.timer.visibility = if (item.alarm) View.VISIBLE else View.GONE

        holder.timer.setOnClickListener {
            schedule = dataItems[position]
            val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
            builder.setMessage("Выключить оповещение на ${holder.time.text}?")
            builder.setPositiveButton("Да") { _, _ ->
                schedule.alarm = false
                MainRepository.updateSchedule(schedule)
            }
            builder.setNegativeButton("Отмена") { _, _ ->
            }
            val dialog: androidx.appcompat.app.AlertDialog = builder.create()
            dialog.show()
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            schedule = dataItems[position]
            if (isChecked) {
                schedule.checkBoxDone = true
                MainRepository.updateSchedule(schedule)
            } else {
                schedule.checkBoxDone = false
                MainRepository.updateSchedule(schedule)
            }
        }

        holder.itemView.setOnLongClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
            builder.setMessage("Удалить ${holder.title_schedule.text} из расписания?")
            builder.setPositiveButton("Да") { _, _ ->
                MainRepository.deleteSchedule(item.id)
            }
            builder.setNegativeButton("Отмена") { _, _ ->
            }
            val dialog: androidx.appcompat.app.AlertDialog = builder.create()
            dialog.show()
            true
        }
    }

    fun updateData(data: List<Schedule>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                dataItems[oldPos].id == data[newPos].id

            override fun getOldListSize(): Int = dataItems.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                dataItems[oldPos].hashCode() == data[newPos].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        dataItems = data
        //  notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title_schedule: TextView = itemView.findViewById(R.id.tv_title_today)
        var date_schedule: TextView = itemView.findViewById(R.id.date_vremenno)
        var time: TextView = itemView.findViewById(R.id.tv_hours_min)
        var timer: ImageView = itemView.findViewById(R.id.iv_timer)
        var checkBox: CheckBox = itemView.findViewById(R.id.cb_done)
    }
}
