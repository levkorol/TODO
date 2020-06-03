package com.levkorol.todo.ui.schedule

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.R
import com.levkorol.todo.model.Schedule

class TouchHelperCallback(val adapter: ScheduleAdapter,
                                  val swipeListener: (Schedule) -> Unit) :
    ItemTouchHelper.Callback() {

    private val bgRect = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if(viewHolder is ItemTouchViewHolder) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        } else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.dataItems[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) { //zazhimaetsya element
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is ItemTouchViewHolder) viewHolder.onItemCleared()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dY: Float,
        dX: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            drawBackground(canvas, itemView, dY)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawIcon(canvas, itemView, dY)
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dY, dX, actionState, isCurrentlyActive)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawIcon(canvas: Canvas, itemView: View, dY: Float) {
        val icon = itemView.resources.getDrawable(R.drawable.ic_delete, itemView.context.theme)
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2
        with(iconBounds) {
            left = itemView.right + dY.toInt() + space
            top = itemView.top + margin
            right = itemView.right + dY.toInt() + iconSize + space
            bottom = itemView.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)

    }

    private fun drawBackground(canvas: Canvas, itemView: View, dY: Float) {
        with(bgRect) {
            left = dY
            top = itemView.top.toFloat()
            left = itemView.left.toFloat()
            bottom = itemView.bottom.toFloat()
        }

        with(bgPaint) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = itemView.resources.getColor(R.color.color_primary_dark, itemView.context.theme)
            }
        }

        canvas.drawRect(bgRect, bgPaint)
    }
}

interface ItemTouchViewHolder{
    fun onItemSelected()
    fun onItemCleared()
}