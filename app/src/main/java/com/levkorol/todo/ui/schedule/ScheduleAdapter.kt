package com.levkorol.todo.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.ui.MainActivity
import androidx.recyclerview.widget.DiffUtil
import com.levkorol.todo.model.Schedule


class ScheduleAdapter(
    val activity: MainActivity
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

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
        holder.date_schedule.text = item.date.toString()
        holder.checkBox.isSelected = item.checkBoxDone
        holder.timer.isSelected = item.alarm


//            holder.itemView.setOnClickListener {
//                activity.loadFragment(
//                    ....Fragment.newInstance(
//                        item
//                    )
//                )
//            }
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
        var date_schedule: TextView = itemView.findViewById(R.id.tv_hours_min)
        var timer: ImageView = itemView.findViewById(R.id.iv_timer)
        var checkBox: CheckBox = itemView.findViewById(R.id.cb_done)
    }
}
