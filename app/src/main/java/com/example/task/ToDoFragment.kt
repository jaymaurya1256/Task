package com.example.task

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.adapters.PendingAdapter
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.ClickType
import com.example.task.models.TaskViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ToDoFragment : Fragment() {

    private val sharedViewModel: TaskViewModel by activityViewModels()

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewForFragmentToDo.layoutManager = LinearLayoutManager(activity)

        sharedViewModel.pendingTask.observe(viewLifecycleOwner) {
            binding.recyclerViewForFragmentToDo.adapter = PendingAdapter(it) { task, clickType ->
                when(clickType){
                    ClickType.SHORT -> sharedViewModel.markCompleted(task)
                    ClickType.LONG_DELETE -> sharedViewModel.deleteTask(task)
                    else -> {
                        binding.contentToDo.visibility = View.GONE
                        binding.includedEditTextField.editTaskField.visibility = View.VISIBLE
                        binding.includedEditTextField.editText.setText(task.task)
                        binding.includedEditTextField.editButton.setOnClickListener{
                            sharedViewModel.editTask(task,binding.includedEditTextField.editText.text.toString())
                            binding.includedEditTextField.editTaskField.visibility = View.GONE
                            binding.contentToDo.visibility = View.VISIBLE
                            Toast.makeText(context, "Task Edited", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.inputTask.setOnClickListener {
            var isTimerSet: Boolean = false
            binding.inputTask.visibility = View.INVISIBLE
            binding.includedInputField.insertTask.visibility = View.VISIBLE
            binding.includedInputField.addDate.setOnClickListener {
                //binding.includedInputField.timePicker.visibility = View.VISIBLE
                fun timePicker() {
                    picker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Set Reminder")
                        .build()
                    picker.show(requireActivity().supportFragmentManager, "Hello")
                    picker.addOnPositiveButtonClickListener {
                        calendar = Calendar.getInstance()
                        calendar[Calendar.HOUR_OF_DAY] = picker.hour
                        calendar[Calendar.MINUTE] = picker.minute
                        calendar[Calendar.SECOND] = 0
                        isTimerSet = true
                    }
                }
                timePicker()
            }
            binding.includedInputField.addTask.setOnClickListener{
                val task: String = binding.includedInputField.inputTaskField.text.toString().trim()
                if (task == ""){
                    binding.inputTask.visibility = View.VISIBLE
                    binding.includedInputField.insertTask.visibility = View.INVISIBLE
                    Toast.makeText(this.context,"Can not add the empty task",Toast.LENGTH_SHORT).show()
                }
                else{
                    sharedViewModel.insert(task)
                    if (isTimerSet) {
                        sharedViewModel.setAlarm(calendar)
                    }
                    binding.includedInputField.inputTaskField.setText("")
                    val view = requireActivity().currentFocus
                    val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken,0)
                    binding.includedInputField.timePicker.visibility = View.GONE
                    binding.includedInputField.insertTask.visibility = View.INVISIBLE
                    binding.inputTask.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}