package com.sahanmondal.taskmanager.ui.tasks.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sahanmondal.taskmanager.R
import com.sahanmondal.taskmanager.databinding.FragmentAddEditTaskBinding
import com.sahanmondal.taskmanager.ui.tasks.viewmodels.AddEditTaskViewModel
import com.sahanmondal.taskmanager.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)
        binding.apply {
            etAddTask.setText(viewModel.taskName)
            cbPriority.isChecked = viewModel.taskPriority
            cbPriority.jumpDrawablesToCurrentState()
            tvDate.isVisible = viewModel.task != null
            tvDate.text = "Created on ${viewModel.task?.createdDateFormatted}"

            etAddTask.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            cbPriority.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskPriority = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.etAddTask.clearFocus()
                        setFragmentResult(
                            "add_edit_req",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}