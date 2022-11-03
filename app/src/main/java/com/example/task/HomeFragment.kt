package com.example.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.task.CompletedFragment
import com.example.task.ToDoFragment
import com.example.task.adapters.ViewPager2Adapter
import com.example.task.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFragment = listOf(ToDoFragment(), CompletedFragment())
        binding.viewPager2.viewPager.adapter = ViewPager2Adapter(listOfFragment, this)
    }
}