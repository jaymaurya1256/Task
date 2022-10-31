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
import com.example.task.databinding.FragmentAddTaskBinding
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.Priority
import com.example.task.models.TaskViewModel
import com.example.task.models.hide
import com.example.task.models.showKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

private const val TAG = "AddTaskFragment"

class AddTaskFragment: BottomSheetDialogFragment(){
    private lateinit var binding: FragmentAddTaskBinding
    val sharedViewModel : TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: Entered OnViewCreated")
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
                }
            }

            this.lowP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.LOW
            }

            this.mediumP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.MEDIUM
            }

            this.highP.setOnCheckedChangeListener { _, selected ->
                if (selected) priority = Priority.HIGH
            }

            this.buttonPickTime.setOnClickListener {
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
                    Log.d(TAG, "onViewCreated: User picked time: $hour:$minute")
                }
            }
        }


    }
}