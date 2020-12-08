package com.levkorol.todo.ui.schedule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Schedule

class ScheduleViewModel : ViewModel() {
    fun getSchedules(): LiveData<List<Schedule>> = MainRepository.getSchedules()
}
