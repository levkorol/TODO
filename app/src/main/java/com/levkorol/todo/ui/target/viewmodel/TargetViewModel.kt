package com.levkorol.todo.ui.target.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets

class TargetViewModel : ViewModel() {
    fun getTargets(): LiveData<List<Targets>> = MainRepository.getTargets()
}
