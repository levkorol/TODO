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
import com.levkorol.todo.utils.DiffCallback
import java.util.*


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var data: List<Note> = listOf()
        set(value) {
            val diffCallback = DiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]



        holder.title.text = item.name
        holder.description.text = item.description
        holder.date.text = item.date
        holder.star.visibility = if (item.star) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            (getActivity(holder.itemView) as MainActivity).loadFragment(
                NoteFragment.newInstance(
                    item
                )
            )
        }
    }

    private fun getActivity(view: View): Activity? {
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = (context).baseContext
        }
        return null
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name_note_text)
        val description: TextView = itemView.findViewById(R.id.text_note)
        val date: TextView = itemView.findViewById(R.id.date_text)
        val star: ImageView = itemView.findViewById(R.id.star_image)

    }
}


