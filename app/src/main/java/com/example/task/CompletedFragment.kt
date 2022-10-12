package com.example.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.adapters.CompletedAdapter
import com.example.task.database.DBHolder
import com.example.task.database.TaskDatabase
import com.example.task.databinding.FragmentCompletedBinding
import com.example.task.models.ClickType
import com.example.task.models.TaskViewModel
import kotlinx.coroutines.launch
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CompletedFragment : Fragment() {

    private val sharedViewModel : TaskViewModel by activityViewModels()
    private var _binding: FragmentCompletedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewForFragmentCompleted.layoutManager = LinearLayoutManager(activity)
        lifecycleScope.launch{
            sharedViewModel.completedTask.observe(viewLifecycleOwner) {
                binding.recyclerViewForFragmentCompleted.adapter = CompletedAdapter(it) { task, clickType, view ->
                    if (ClickType.SHORT == clickType) {
                        sharedViewModel.markCompleted(task)
                    } else {
                        val popupMenu: PopupMenu = PopupMenu(view.context,view)
                        popupMenu.menuInflater.inflate(R.menu.item_list_menu,popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener {
                            when(it.itemId){
                                R.id.delete -> { sharedViewModel.deleteTask(task)
                                true }
                                R.id.edit -> { sharedViewModel.editTask(task, "Hello")
                                true }
                                else -> true
                            }
                        }
                        popupMenu.show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}