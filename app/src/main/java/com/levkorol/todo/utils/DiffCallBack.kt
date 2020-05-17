package com.levkorol.todo.utils


import androidx.recyclerview.widget.DiffUtil
import com.levkorol.todo.model.Note

class DiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
//                && oldList[oldItemPosition].name == newList[newItemPosition].name
//                && oldList[oldItemPosition].description == newList[newItemPosition].description
    }
}