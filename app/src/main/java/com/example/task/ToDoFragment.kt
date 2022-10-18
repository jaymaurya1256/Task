package com.example.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.adapters.PendingAdapter
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.databinding.ActivityMainBinding
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.databinding.TaskListBinding
import com.example.task.models.ClickType
import com.example.task.models.TaskViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ToDoFragment : Fragment() {

    private val sharedViewModel: TaskViewModel by activityViewModels()

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

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
            binding.inputTask.visibility = View.INVISIBLE
            binding.includedInputField.insertTask.visibility = View.VISIBLE
            binding.includedInputField.addTask.setOnClickListener{
                val task: String = binding.includedInputField.inputTaskField.text.toString().trim()
                if (task == ""){
                    binding.inputTask.visibility = View.VISIBLE
                    binding.includedInputField.insertTask.visibility = View.INVISIBLE
                    Toast.makeText(this.context,"Can not add the empty task",Toast.LENGTH_SHORT).show()
                }
                else{
                    sharedViewModel.insert(task)
                    binding.includedInputField.inputTaskField.setText("")
                    binding.inputTask.visibility = View.VISIBLE
                    binding.includedInputField.insertTask.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}