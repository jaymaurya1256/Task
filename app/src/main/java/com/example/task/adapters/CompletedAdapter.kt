package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.TaskViewModel


class CompletedAdapter(private val task: List<Task>, private val lambdaMarkInComplete: () -> TaskViewModel)
    : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    class CompletedViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.listItemTextField)
        val taskList: ConstraintLayout = view.findViewById(R.id.list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list ,parent,false)
        return CompletedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        holder.textView.text = task[position].task
        holder.taskList.setOnClickListener{
            lambdaMarkInComplete.invoke().markIncomplete(task[position])
        }
        holder.taskList.setOnLongClickListener { true }
    }

    override fun getItemCount(): Int {
        return task.size
    }

}