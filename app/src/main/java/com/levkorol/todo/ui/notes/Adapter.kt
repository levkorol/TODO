package com.levkorol.todo.ui.notes

import android.content.Context
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
import com.levkorol.todo.ui.note.NoteFragment

class Adapter(val activity: MainActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflator: LayoutInflater? = null
    private val context: Context? = null
    private val list: List<Base>? = null

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

    init {
        this.data = this.list!!
        this.mInflator = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NOTE_VIEW_TYPE) {
            NoteViewHolder(
                mInflator!!.inflate(
                    R.layout.list_item_note,
                    parent,
                    false
                )
            )
        } else FolderViewHolder(
            mInflator!!.inflate(
                R.layout.list_item_folder,
                parent,
                false
            )
        )
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val view = layoutInflater.inflate(
//            if (viewType == NotesAdapter.NOTE_VIEW_TYPE) R.layout.list_item_note else R.layout.list_item_folder,
//            parent, false
//        )
    }

    private inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameFolder: TextView = itemView.findViewById(R.id.name_folder_text)
        val color: Unit = itemView.setBackgroundColor(R.id.background_folder_list)
    }


    private inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name_note_text)
        val description: TextView = itemView.findViewById(R.id.text_note)
        val date: TextView = itemView.findViewById(R.id.date_text)
        val star: ImageView = itemView.findViewById(R.id.star_image)
    }
}






