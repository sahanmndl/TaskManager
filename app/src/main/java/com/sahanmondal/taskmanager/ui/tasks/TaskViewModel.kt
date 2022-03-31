package com.sahanmondal.taskmanager.ui.tasks

import androidx.lifecycle.ViewModel
import com.sahanmondal.taskmanager.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
}