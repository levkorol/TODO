package com.levkorol.todo.ui.notes


import androidx.recyclerview.widget.DiffUtil
import com.levkorol.todo.model.Note

class DiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].noteId == newList[newItemPosition].noteId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].noteId == newList[newItemPosition].noteId
                && oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].description == newList[newItemPosition].description
    }
}