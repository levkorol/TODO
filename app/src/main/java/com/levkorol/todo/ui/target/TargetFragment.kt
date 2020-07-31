package com.levkorol.todo.ui.target

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.levkorol.todo.R
import kotlinx.android.synthetic.main.target_fragment.*
import java.util.*

class TargetFragment : Fragment() {

    companion object {
        fun newInstance() = TargetFragment()
    }

    private lateinit var viewModel: TargetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.target_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_start.setOnClickListener {
            //todo
        }

        btn_pause.setOnClickListener {
            //todo
        }

        btn_stop.setOnClickListener {
            //todo
        }

        resources.getQuantityString(R.plurals.seconds, 13)
    }

    private val timer by lazy { Timer() }
    private var timerTask: TimerTask? = null
    // TODO завести отдельную переменную для времени момента нажатия (по умолчанию - null или DEFAULT_TIME) epoch

    private fun start() {
        // TODO запоминаем время нажатия
        timerTask = object : TimerTask() {
            override fun run() {
                updateUI()
            }
        }
        timer.schedule(timerTask, 0, 25)
    }

    private fun pause() {
        // TODO
    }

    private fun stop() {
        timerTask?.cancel()
        // TODO сбросить время нажатия
        // TODO обновить интерфейс
    }

    private fun updateUI() {
        Log.v("TargetFragment", "updateUI")
        // TODO считаем разницу между текущим и нажатием
        // TODO обновляешь интерфейс
    }
}
