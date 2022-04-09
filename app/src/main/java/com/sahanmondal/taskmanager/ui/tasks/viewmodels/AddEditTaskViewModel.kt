package com.sahanmondal.taskmanager.ui.tasks.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahanmondal.taskmanager.data.Task
import com.sahanmondal.taskmanager.data.TaskDao
import com.sahanmondal.taskmanager.ui.ADD_TASK_RESULT_OK
import com.sahanmondal.taskmanager.ui.EDIT_TASK_RESULT_OK
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor (
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskPriority = state.get<Boolean>("taskPriority") ?: task?.prioritise ?: false
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Task cannot be empty!")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, prioritise = taskPriority)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, prioritise = taskPriority)
            createNewTask(newTask)
        }
    }

    private fun createNewTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}