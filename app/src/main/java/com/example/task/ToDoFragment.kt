package com.example.task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.adapters.PendingAdapter
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

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
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewForFragmentToDo.layoutManager = LinearLayoutManager(activity)

        sharedViewModel.pendingTask.observe(viewLifecycleOwner) {
            binding.recyclerViewForFragmentToDo.adapter = PendingAdapter(it) { task, clickType ->
                when (clickType) {
                    ClickType.SHORT -> sharedViewModel.markCompleted(task)
                    ClickType.LONG_DELETE -> sharedViewModel.deleteTask(task)
                    else -> {
                        binding.contentToDo.hide()
                        PurposeToNavigate().purposeToNavigate = "EditTask"
                        val id = task.id
                        val action = ToDoFragmentDirection
                        findNavController().navigate(R.id.action_toDoFragment_to_addTaskFragment)

//                        bindingItems!!.buttonAddTask.setText(R.string.edit)
//                        bindingItems!!.textFieldTaskDescription.editText!!.setText(task.task)
//                        when(task.priority) {
//                            "Low" -> bindingItems!!.lowP.isChecked = true
//                            "Medium" -> bindingItems!!.mediumP.isChecked = true
//                            "High" -> bindingItems!!.highP.isChecked = true
//                        }
//                        itemListDialogFragment.show(requireActivity().supportFragmentManager,itemListDialogFragment.tag)
//                        bindingItems!!.buttonAddTask.setOnClickListener{
//                            val hour = TimeUnit.HOURS.convert(task.time, TimeUnit.HOURS)
//                            val minute = TimeUnit.MINUTES.convert(task.time, TimeUnit.MINUTES)
//                            val priority:Priority
//                            when(task.priority) {
//                                "LOW" -> priority = Priority.LOW
//                                "MEDIUM" -> priority = Priority.MEDIUM
//                                "HIGH" -> priority = Priority.HIGH
//                                else -> priority = Priority.LOW
//                            }
//                            val taskDescription = bindingItems!!.textFieldTaskDescription.editText?.text.toString().trim()
//                            if (taskDescription.isEmpty()) {
//                                bindingItems!!.textFieldTaskDescription.error = "Please enter a task"
//                            } else {
//                                sharedViewModel.insertTask(taskDescription, hour, minute, priority)
//                                itemListDialogFragment.dismiss()
//                                Snackbar.make(binding.root, "Task Added", Snackbar.LENGTH_SHORT).show()
//                            }
//                            bindingItems!!.buttonAddTask.setText(R.string.Add)
//                        }
//                    }
                    }
                }
            }

            binding.fabAddTask.setOnClickListener {
                PurposeToNavigate().purposeToNavigate = "InsertTask"
                findNavController().navigate(R.id.action_toDoFragment_to_addTaskFragment)
            }
        }
    }
}