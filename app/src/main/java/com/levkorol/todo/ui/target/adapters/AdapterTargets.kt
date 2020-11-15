package com.levkorol.todo.ui.target.adapters


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets
import com.levkorol.todo.model.Targets.BackgroundTarget.*
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.target.EditTargetFragment
import com.levkorol.todo.utils.Tools


class AdapterTargets(val activity: MainActivity) :
    RecyclerView.Adapter<AdapterTargets.ViewHolderTargets>() {

    var dataItems: List<Targets> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolderTargets, position: Int) {
        val targetItem = dataItems[position]

        if (targetItem.background == ORANGE || targetItem.background == DARKER_BLU || targetItem.background == PURPLE) {
            holder.name.setTextColor(Color.WHITE)
            holder.description.setTextColor(Color.WHITE)
            holder.countDay.setTextColor(Color.WHITE)
            holder.addArchive.setTextColor(Color.WHITE)
            holder.done_in_archiv.setTextColor(Color.CYAN)
            holder.swich_target.setTextColor(Color.WHITE)
        } else {
            holder.name.setTextColor(Color.BLACK)
            holder.description.setTextColor(Color.BLACK)
            holder.countDay.setTextColor(Color.BLACK)
            holder.addArchive.setTextColor(Color.BLUE)
            holder.done_in_archiv.setTextColor(Color.BLACK)
            holder.swich_target.setTextColor(Color.BLACK)
        }

        holder.dateCreate.text = "Дата создания: ${Tools.dateToString(targetItem.startData)}"

        if (targetItem.targetDone) {
            holder.addArchive.visibility = View.VISIBLE
        } else {
            holder.addArchive.visibility = View.GONE
        }
        val result =
            if (targetItem.stopData > 0) {
                targetItem.stopData - targetItem.startData
            } else {
                System.currentTimeMillis() - targetItem.startData
            }

        //(targetItem.stopData != null && targetItem!!.stopData > 0 ? targetItem!!.stopData : System.currentTimeMillis()) - targetItem.startData)
        val days =
            result / 1000 /*количество секунд*/ / 60 /* количество минут */ / 60 /* количество часов */ / 24 /* количество дней */
        val hours = result / 1000 / 60 / 60

        var resultHours: Long = hours % 24
        holder.name.text = targetItem.name
        holder.description.text = targetItem.description
        holder.countDay.text = "Прошло дней: $days ,  часов: $resultHours "
        holder.swich_target.isChecked = targetItem.targetDone

        //targets = dataItems[position]
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


        holder.swich_target.setOnClickListener {
            holder.swich_target.isChecked = true
            if (holder.swich_target.isChecked) {
                targetItem.targetDone = true
                targetItem.stopData = System.currentTimeMillis()
                holder.swich_target.isChecked = true
                MainRepository.updateTarget(targetItem)
            } else {
                targetItem.targetDone = false
                targetItem.stopData = 0
                MainRepository.updateTarget(targetItem)
            }
            if (holder.swich_target.isChecked) {
                Toast.makeText(activity, "Цель выполнена", Toast.LENGTH_LONG).show()
            }
        }

        holder.editTarget.setOnClickListener {
            (activity).loadFragment(EditTargetFragment.newInstance(targetItem.id))
        }

        holder.addArchive.setOnClickListener {
            targetItem.inArchive = true
            //  targetItem.days = days.toInt()
            MainRepository.updateTarget(targetItem)
        }

        if (targetItem.inArchive) {
            holder.addArchive.visibility = View.GONE
            holder.swich_target.visibility = View.GONE
            holder.done_in_archiv.visibility = View.VISIBLE
            holder.countDay.visibility = View.GONE
            holder.done_in_archiv.text = "Дней: ${days}, Часов: ${resultHours} "
        } else {
            holder.swich_target.visibility = View.VISIBLE
            holder.done_in_archiv.visibility = View.GONE
        }

        if (targetItem.image > 0) {
            holder.icon.visibility = View.VISIBLE
        } else {
            holder.icon.visibility = View.GONE
        }
        holder.itemView.setBackgroundResource(targetItem.background.res)

        if (targetItem.image == 1) holder.icon.setImageResource(R.drawable.lev_del_net)
        if (targetItem.image == 2) holder.icon.setImageResource(R.drawable.ic_up)
        if (targetItem.image == 3) holder.icon.setImageResource(R.drawable.ic_repit)
        if (targetItem.image == 4) holder.icon.setImageResource(R.drawable.ic_idea)
        if (targetItem.image == 5) holder.icon.setImageResource(R.drawable.ic_mystars)
        if (targetItem.image == 6) holder.icon.setImageResource(R.drawable.pick8)
        if (targetItem.image == 7) holder.icon.setImageResource(R.drawable.ic_clear)
        if (targetItem.image == 8) holder.icon.setImageResource(R.drawable.ic_access_time)
        if (targetItem.image == 9) holder.icon.setImageResource(R.drawable.ic_as)
        if (targetItem.image == 10) holder.icon.setImageResource(R.drawable.pick_lenivec)
        if (targetItem.image == 11) holder.icon.setImageResource(R.drawable.lion_pick_three)
        if (targetItem.image == 12) holder.icon.setImageResource(R.drawable.ic_hot)
        if (targetItem.image == 13) holder.icon.setImageResource(R.drawable.ic_air)
        if (targetItem.image == 14) holder.icon.setImageResource(R.drawable.ic_eat)
        if (targetItem.image == 15) holder.icon.setImageResource(R.drawable.ic_all_inclusive_black_24dp)
        if (targetItem.image == 16) holder.icon.setImageResource(R.drawable.ic_audiotrack_black_24dp)
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

    class ViewHolderTargets(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_target)
        val description: TextView = itemView.findViewById(R.id.description_target)
        val countDay: TextView = itemView.findViewById(R.id.count_day)

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val swich_target: Switch = itemView.findViewById(R.id.swich_targets)
        val addArchive: TextView = itemView.findViewById(R.id.add_archive)
        val done_in_archiv: TextView = itemView.findViewById(R.id.target_done)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val editTarget: ImageView = itemView.findViewById(R.id.edit_target)
        val dateCreate: TextView = itemView.findViewById(R.id.dateCreate)
    }
}
