package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task


class CompletedAdapter(private val task: List<Task>) : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {
    class CompletedViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.listItemTextField)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list ,parent,false)
        return CompletedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        holder.textView.text = task[position].task
    }

    override fun getItemCount(): Int {
        return task.size
    }

}