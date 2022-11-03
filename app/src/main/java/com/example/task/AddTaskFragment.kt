package com.example.task

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.task.database.Task
import com.example.task.databinding.FragmentAddTaskBinding
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

private const val TAG = "AddTaskFragment"

class AddTaskFragment: BottomSheetDialogFragment(){
    private lateinit var binding: FragmentAddTaskBinding
    private val sharedViewModel : TaskViewModel by activityViewModels()
    private val args by navArgs<AddTaskFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: Entered OnViewCreated")

        // If ID is -1, we're in add mode
        // If ID != -1, we're in edit mode
        if (args.taskId == -1){
            insertTask()
        } else {
            sharedViewModel.getTask(args.taskId)
            sharedViewModel.task.observe(viewLifecycleOwner) {
                editTask(it)
            }
        }

    }

    private fun insertTask() {
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
                    Snackbar.make(binding.root, "Task Added", Snackbar.LENGTH_SHORT).show()
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }

            lowP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.LOW
            }

            mediumP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.MEDIUM
            }

            highP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.HIGH
            }

            buttonPickTime.setOnClickListener {
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                    .setTitleText("Select Time")
                    .build()
                picker.show(childFragmentManager, "TimePicker")
                picker.addOnPositiveButtonClickListener {
                    hour = picker.hour.toLong()
                    minute = picker.minute.toLong()
                    buttonPickTime.text = "Alarm set at $hour:$minute"
                }
            }
        }
    }

    private fun editTask(task: Task) {
        with(binding){
            buttonAddTask.setText(R.string.edit)
            var hour = SimpleDateFormat("hh", Locale.getDefault()).format(task.time).toLong()
            var minute = SimpleDateFormat("mm", Locale.getDefault()).format(task.time).toLong()
            var priority = when(task.priority) {
                "LOW" -> Priority.LOW
                "Medium" -> Priority.MEDIUM
                else -> Priority.HIGH

            }
            textFieldTaskDescription.editText?.setText(task.task)
            buttonAddTask.setOnClickListener {
                val taskDescription = textFieldTaskDescription.editText?.text.toString().trim()
                if (taskDescription.isEmpty()) {
                    textFieldTaskDescription.error = "Please enter a task"
                } else {
                    sharedViewModel.editTask(task.id,taskDescription, hour, minute, priority)
                    Snackbar.make(binding.root, "Task Edited", Snackbar.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
            lowP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.LOW
            }

            mediumP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.MEDIUM
            }

            highP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.HIGH
            }

            buttonPickTime.setOnClickListener {
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                    .setTitleText("Select Time")
                    .build()
                picker.show(childFragmentManager, "TimePicker")
                picker.addOnPositiveButtonClickListener {
                    hour = picker.hour.toLong()
                    minute = picker.minute.toLong()
                    buttonPickTime.text = "Alarm set at $hour:$minute"
                }
            }

        }
    }
}