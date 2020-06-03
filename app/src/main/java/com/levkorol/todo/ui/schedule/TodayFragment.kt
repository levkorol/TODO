package com.levkorol.todo.ui.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.edit_note_fragment.*
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.item_list_schedule.*
import kotlinx.android.synthetic.main.item_list_schedule.view.*
import kotlinx.android.synthetic.main.schedule_fragment.*


class TodayFragment : Fragment() {

    private lateinit var adapter: ScheduleAdapter
    private lateinit var schedule: Schedule
    private lateinit var viewModel: ScheduleViewModel
    private var id: Long = -1

    companion object {
        private const val SCHEDULE_ID = "SCHEDULE_ID"
        fun newInstance(id: Long): TodayFragment {
            val fragment = TodayFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(SCHEDULE_ID, id)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            id = arguments?.getLong(SCHEDULE_ID, id)!!
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_today)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = ScheduleAdapter(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter

//        cb_done.setOnCheckedChangeListener { buttonView, isChecked ->
//            schedule.checkBoxDone = isChecked
//        }
//        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//
//        val touchCallback = TouchHelperCallback(adapter) {
//            //если лямбда является последним аргументом можно вынести ее за скобки
//           // viewModel.addToArchive(it.id)
//            MainRepository.deleteSchedule(id)
//            Snackbar.make(
//                recycler_view_today,
//                "Вы точно хотите  ${it.title} delelte?",
//                Snackbar.LENGTH_LONG
//            ).show()
//        }
//        val touchHelper = ItemTouchHelper(touchCallback)
//        touchHelper.attachToRecyclerView(recycler_view_today)
//
//        with(recycler_view_today) {
//            adapter = adapter
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(divider)
//        }
    }

    override fun onStart() {
        super.onStart()
        viewModel =
            ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        observeSchedule()
    }

    private fun observeSchedule() {
        viewModel.getSchedules().observe(this, Observer { schedule ->

            val task = schedule.firstOrNull { s -> s.id == id }
            task?.checkBoxDone

            adapter.dataItems = schedule

            adapter.notifyDataSetChanged()
        })
    }

}
