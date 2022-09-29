package com.example.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.TaskViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ToDoFragment : Fragment() {

    private val sharedViewModel: TaskViewModel by activityViewModels()

    private var _binding: FragmentTodoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.addTask.setOnClickListener {
            val task = binding.inputTask.text.toString()
            sharedViewModel.insert(task)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}