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
            holder.date.text = item.date.toString()
            holder.star.visibility = if (item.star) View.VISIBLE else View.GONE
            holder.itemView.setOnClickListener {
                activity.loadFragment(NoteFragment.newInstance(item))
            }
        } else {
            item as Folder
            holder as FolderViewHolder
            holder.nameFolder.text = item.nameFolder
            holder.itemView.setOnClickListener {
                activity.loadFragment(NotesFragment())
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
   // val color: Unit = itemView.setBackgroundColor(R.id.background_folder_list)
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.name_note_text)
    val description: TextView = itemView.findViewById(R.id.text_note)
    val date: TextView = itemView.findViewById(R.id.date_text)
    val star: ImageView = itemView.findViewById(R.id.star_image)
}






