package com.levkorol.todo.ui.target

import android.util.Log
import com.levkorol.todo.utils.DEFAULT_DATE
import java.util.*

  object TimerTarget {
      private val timer by lazy { Timer() }
      private var timerTask: TimerTask? = null
      var dateStart = DEFAULT_DATE

      fun start() {
          // TODO запоминаем время нажатия
          dateStart = System.currentTimeMillis()
          timerTask = object : TimerTask() {
              override fun run() {
                  updateUI()
              }
          }
          timer.schedule(timerTask, 0, 25)
      }

      private fun stop() {
          timerTask?.cancel()
          dateStart = DEFAULT_DATE
          // TODO обновить интерфейс
      }

      private fun updateUI() {
          Log.v("TargetFragment", "updateUI")
          val result = System.currentTimeMillis() - dateStart
          // TODO считаем разницу между текущим и нажатием
          // TODO обновляешь интерфейс
      }
  }
