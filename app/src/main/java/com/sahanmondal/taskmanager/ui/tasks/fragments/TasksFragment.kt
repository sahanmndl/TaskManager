package com.sahanmondal.taskmanager.ui.tasks.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sahanmondal.taskmanager.R
import com.sahanmondal.taskmanager.ui.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TaskViewModel by viewModels()
}