package com.levkorol.todo.ui.notes



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.folder.FolderFragment
import com.levkorol.todo.ui.note.NoteFragment
import com.levkorol.todo.utils.Tools

class Adapter(val activity: MainActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Base> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = data[position]

        if (item is Note) {
            holder as NoteViewHolder
            holder.title.text = item.name
            holder.description.text = item.description
            holder.star.visibility = if (item.star) View.VISIBLE else View.GONE
            if(item.addSchedule) {
                holder.text_date.visibility = View.VISIBLE
                holder.text_date.text = item.dateSchedule.toString()
                holder.text_time.visibility = View.VISIBLE
                holder.text_time.text = item.time.toString()
                holder.text_date.text = Tools.dateToStringtwo(item.date)
                holder.text_time.text = Tools.convertLongToTimeString(item.time)

            }
            holder.itemView.setOnClickListener {
                activity.loadFragment(NoteFragment.instance(item.id))
            }
        } else {
            item as Folder
            holder as FolderViewHolder
            holder.nameFolder.text = item.nameFolder
            holder.itemView.setBackgroundResource(item.background.res)
            holder.itemView.setOnClickListener {
                activity.loadFragment(FolderFragment.newInstance(item))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if (item is Note) NOTE_VIEW_TYPE else FOLDER_VIEW_TYPE
    }

    companion object {
        private const val NOTE_VIEW_TYPE = 0
        private const val FOLDER_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            NOTE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_note, parent, false)
                NoteViewHolder(view)
            }
            FOLDER_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_folder, parent, false)
                FolderViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}

class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameFolder: TextView = itemView.findViewById(R.id.name_folder_text)
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.name_note_text)
    val description: TextView = itemView.findViewById(R.id.text_note)
    val date: TextView = itemView.findViewById(R.id.date_text)
    val star: ImageView = itemView.findViewById(R.id.star_image)
    val text_time: TextView = itemView.findViewById(R.id.text_time)
    val text_date: TextView = itemView.findViewById(R.id.text_date)
}







