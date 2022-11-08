package com.example.task

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.adapters.PendingAdapter
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.*
import com.google.android.material.timepicker.MaterialTimePicker

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
private const val TAG = "ToDoFragment"

class ToDoFragment : Fragment() {

    private val sharedViewModel: TaskViewModel by activityViewModels()


    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewForFragmentToDo.layoutManager = LinearLayoutManager(activity)

        sharedViewModel.pendingTask.observe(viewLifecycleOwner) {
            binding.recyclerViewForFragmentToDo.adapter = PendingAdapter(it) { task, clickType ->
                when (clickType) {
                    ClickType.SHORT -> sharedViewModel.markCompleted(task)
                    ClickType.LONG_DELETE -> sharedViewModel.deleteTask(task)
                    else -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToAddTaskFragment(task.id)
                        findNavController().navigate(action)
                    }
                }
            }

            binding.fabAddTask.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
            }
            binding.info.setOnClickListener{
                val intent = Intent(this.context, PopUpWindow::class.java)
                startActivity(intent)
            }
            binding.clearAll.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setMessage(R.string.delete_all)
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton(android.R.string.yes) { dialog, which ->
                    sharedViewModel.deleteAllFromPending()
                }

                alertDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                    dialog.cancel()
                }
                alertDialog.show()
            }
        }
    }
}