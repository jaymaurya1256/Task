package com.example.task

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lottieAnimationView = binding.lottieCompleted
        sharedViewModel.completedTask.observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.setAnimation(R.raw.completed_animation)
                lottieAnimationView.loop(true)
                lottieAnimationView.playAnimation()
            }
            else{
                lottieAnimationView.visibility = View.GONE
            }
        }

        binding.recyclerViewForFragmentCompleted.layoutManager = LinearLayoutManager(activity)
        lifecycleScope.launch{
            sharedViewModel.completedTask.observe(viewLifecycleOwner) {
                binding.recyclerViewForFragmentCompleted.adapter = CompletedAdapter(it) { task, clickType, view ->
                    when(clickType){
                        ClickType.SHORT -> {sharedViewModel.markIncomplete(task)}
                        else -> {sharedViewModel.deleteTask(task)}
                    }
                }
            }
        }
        binding.clearAll.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setMessage(R.string.delete_all_from_completed)
            alertDialog.setPositiveButton(android.R.string.yes){ _, _ ->
                sharedViewModel.deleteAllFromCompleted()
            }
            alertDialog.setNegativeButton(android.R.string.no){ dialog,_ ->
                dialog.cancel()
            }
            alertDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}