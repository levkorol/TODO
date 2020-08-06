package com.levkorol.todo.ui.target.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.schedule.adapters.ScheduleAdapterToday
import com.levkorol.todo.ui.target.TimerTarget
import com.levkorol.todo.utils.Tools

class AdapterTargets(val activity: MainActivity) :
    RecyclerView.Adapter<AdapterTargets.ViewHolderTargets>() {

    private lateinit var targets: Targets

    var dataItems: List<Targets> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolderTargets, position: Int) {
        val resultDay = System.currentTimeMillis() - TimerTarget.dateStart


        val targetItem = dataItems[position]

        holder.name.text = targetItem.name
        holder.description.text = targetItem.description
        holder.countDay.text = "Прошло ${Tools.timeToString(resultDay)} дней"
        targets = dataItems[position]
        holder.itemView.setOnLongClickListener {
            val builder = MaterialAlertDialogBuilder(activity)
            builder.setMessage("Удалить: ${holder.name.text} ?")
            builder.setPositiveButton("Да") { _, _ ->
                MainRepository.deleteTarget(targetItem.id)
            }
            builder.setNegativeButton("Отмена") { _, _ ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }

      //  holder.swich_target.isChecked = true
        holder.swich_target.setOnClickListener {
            if (holder.swich_target.isChecked) {
                holder.swich_target.isChecked = true
                targets.targetDone = true
                MainRepository.updateTarget(targets)
            } else {
                holder.swich_target.isChecked = false
                targets.targetDone = false
                MainRepository.updateTarget(targets)
            }
            if (targets.targetDone) {
                holder.addArchive.visibility = View.VISIBLE
            } else {
                holder.addArchive.visibility = View.GONE
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTargets {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.item_target_list,
            parent, false
        )
        return ViewHolderTargets(view)
    }

    override fun getItemCount(): Int = dataItems.size

    private fun updateUI() {
        Log.v("TargetFragment", "updateUI")
        val result = System.currentTimeMillis() - TimerTarget.dateStart
        // TODO считаем разницу между текущим и нажатием

        // TODO обновляешь интерфейс
    }
    class ViewHolderTargets(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_target)
        val description: TextView = itemView.findViewById(R.id.description_target)
        val countDay: TextView = itemView.findViewById(R.id.count_day)
        val swich_target: Switch = itemView.findViewById(R.id.swich_targets)
        val addArchive: TextView = itemView.findViewById(R.id.add_archive)
    }
}
