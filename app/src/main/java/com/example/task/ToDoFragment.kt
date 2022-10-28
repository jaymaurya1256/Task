package com.example.task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.adapters.PendingAdapter
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.ClickType
import com.example.task.models.Priority
import com.example.task.models.TaskViewModel
import com.example.task.models.hide
import com.example.task.models.hideKeyboard
import com.example.task.models.show
import com.example.task.models.showKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
private const val TAG = "ToDoFragment"

class ToDoFragment : Fragment() {

    private val sharedViewModel: TaskViewModel by activityViewModels()

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var picker: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewForFragmentToDo.layoutManager = LinearLayoutManager(activity)

        binding.fabAddTask.setOnClickListener {
            binding.layoutTaskInput.show()
            binding.textFieldTaskDescription.requestFocus()
            showKeyboard()
            binding.fabAddTask.hide()
        }

        sharedViewModel.pendingTask.observe(viewLifecycleOwner) {
            binding.recyclerViewForFragmentToDo.adapter = PendingAdapter(it) { task, clickType ->
                when (clickType) {
                    ClickType.SHORT -> sharedViewModel.markCompleted(task)
                    ClickType.LONG_DELETE -> sharedViewModel.deleteTask(task)
                    else -> {
                        binding.contentToDo.hide()
                        binding.includedEditTextField.layoutEditTask.show()
                        binding.includedEditTextField.editTextField.editText?.setText(task.task)
                        binding.includedEditTextField.editButton.setOnClickListener {
                            sharedViewModel.editTask(
                                task,
                                binding.includedEditTextField.editTextField.editText?.text.toString()
                            )
                            binding.includedEditTextField.layoutEditTask.hide()
                            binding.contentToDo.show()
                            Toast.makeText(context, "Task Edited", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        with (binding) {
            var hour = -1L
            var minute = -1L
            var priority = Priority.LOW
            buttonAddTask.setOnClickListener {
                val taskDescription = textFieldTaskDescription.editText?.text.toString().trim()
                if (taskDescription.isEmpty()) {
                    textFieldTaskDescription.error = "Please enter a task"
                } else {
                    sharedViewModel.insertTask(taskDescription, hour, minute, priority)
                    reset()
                    Snackbar.make(binding.root, "Task Added", Snackbar.LENGTH_SHORT).show()
                }
            }

            lowP.setOnCheckedChangeListener { _ , selected ->
                if (selected) priority = Priority.LOW
            }

            mediumP.setOnCheckedChangeListener { _ , selected ->
                if (selected) priority = Priority.MEDIUM
            }

            highP.setOnCheckedChangeListener { _ , selected ->
                if (selected) priority = Priority.HIGH
            }

            buttonPickTime.setOnClickListener {
                picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                    .setTitleText("Select Time")
                    .build()
                picker.show(childFragmentManager, "TimePicker")
                picker.addOnPositiveButtonClickListener {
                    hour = picker.hour.toLong()
                    minute = picker.minute.toLong()
                    Log.d(TAG, "onViewCreated: User picked time: $hour:$minute")
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (binding.layoutTaskInput.isVisible) {
                reset()
            } else if (binding.includedEditTextField.root.isVisible) {
                binding.includedEditTextField.layoutEditTask.hide()
                binding.contentToDo.show()
            } else requireActivity().finish()
        }

    }

    private fun reset() {
        hideKeyboard()
        binding.textFieldTaskDescription.editText?.setText("")
        binding.lowP.isChecked = true
        binding.layoutTaskInput.hide()
        binding.fabAddTask.show()
    }
}