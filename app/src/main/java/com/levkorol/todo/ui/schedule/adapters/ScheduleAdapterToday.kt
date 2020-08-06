package com.levkorol.todo.ui.schedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.utils.Tools

class ScheduleAdapterToday(
    val activity: MainActivity
) : RecyclerView.Adapter<ScheduleAdapterToday.ScheduleViewHolder>() {

    private lateinit var schedule: Schedule

    var dataItems: List<Schedule> = listOf()
        set(value) {
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

        holder.title_schedule.text = item.description

        if(!item.addTime) {
            holder.time.visibility = View.GONE
            //holder.min.visibility = View.GONE
           // holder.timer.visibility = View.GONE
        } else {
            holder.time.visibility = View.VISIBLE
            holder.time.text = Tools.convertLongHoursAndMinutesToString(item.hours, item.minutes)
        }
        if(item.alarm ) {
            holder.timer.visibility = View.VISIBLE
          //  holder.time.visibility = View.VISIBLE
        } else {
            holder.timer.visibility = View.GONE
           // holder.time.visibility = View.GONE
        }


        holder.checkBox.setOnCheckedChangeListener (null)
        holder.checkBox.isChecked = item.checkBoxDone
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
            val builder = MaterialAlertDialogBuilder(activity)
            builder.setMessage("Удалить: ${holder.title_schedule.text} ?")
            builder.setPositiveButton("Да") { _, _ ->
                MainRepository.deleteSchedule(item.id)
            }
            builder.setNegativeButton("Отмена") { _, _ ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title_schedule: TextView = itemView.findViewById(R.id.tv_title_today)
        var time: TextView = itemView.findViewById(R.id.tv_hours_min)
      //  var min: TextView = itemView.findViewById(R.id.tv_min)
        var timer: ImageView = itemView.findViewById(R.id.iv_timer)
        var checkBox: CheckBox = itemView.findViewById(R.id.cb_done)

    }
}
