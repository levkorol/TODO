package com.levkorol.todo.base

import androidx.recyclerview.widget.RecyclerView

interface DraggableListDelegate {
    fun startDragging(viewHolder: RecyclerView.ViewHolder)
}