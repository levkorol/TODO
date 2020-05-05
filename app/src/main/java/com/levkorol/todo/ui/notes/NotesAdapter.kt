package com.levkorol.todo.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Note
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.note.NoteFragment
import android.content.ContextWrapper
import android.app.Activity
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.levkorol.todo.model.Base
import com.levkorol.todo.model.Folder
import com.levkorol.todo.ui.folder.FolderFragment
import com.levkorol.todo.utils.DiffCallback


class NotesAdapter(
    val activity: MainActivity
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    var data: List<Base> = listOf() // [ Folder, Note, Folder, Note ]
        set(value) {
//            val diffCallback = DiffCallback(field, value)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            diffResult.dispatchUpdatesTo(this)
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if (item is Note) NOTE_VIEW_TYPE else FOLDER_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            if (viewType == NOTE_VIEW_TYPE) R.layout.list_item_note else R.layout.list_item_folder,
            parent, false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = data[position]
        if (item is Note) {
            holder.title.text = item.name
            holder.description.text = item.description
            holder.date.text = item.date.toString()
            holder.star.visibility = if (item.star) View.VISIBLE else View.GONE
            holder.itemView.setOnClickListener {
                activity.loadFragment(
                    NoteFragment.newInstance(
                        item
                    )
                )
            }
        } else {
            item as Folder
            holder as FolderViewHolder
            holder.nameFolder.text = item.nameFolder
//            holder.itemView.setOnClickListener {
//                activity.loadFragment(
//                    FolderFragment.newInstance(
//                        item
//                    )
//                )
//            }
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name_note_text)
        val description: TextView = itemView.findViewById(R.id.text_note)
        val date: TextView = itemView.findViewById(R.id.date_text)
        val star: ImageView = itemView.findViewById(R.id.star_image)
    }

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameFolder: TextView = itemView.findViewById(R.id.name_folder_text)
        val color: Unit = itemView.setBackgroundColor(R.id.background_folder_list)
    }

    companion object {
        private const val NOTE_VIEW_TYPE = 0
        private const val FOLDER_VIEW_TYPE = 1
    }

}


