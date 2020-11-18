package com.levkorol.todo

import androidx.recyclerview.widget.RecyclerView

interface DraggableListDelegate {
    fun startDragging(viewHolder: RecyclerView.ViewHolder)
}