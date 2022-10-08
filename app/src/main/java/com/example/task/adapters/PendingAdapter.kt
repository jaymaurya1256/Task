package com.example.task.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.TaskViewModel

class PendingAdapter(private val taskList: List<Task>,private val lambdaMarkCompleted: ()->TaskViewModel) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
        val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.textView.text = taskList[position].task
        holder.listItem.setOnClickListener {
            //lambda implementation should be there i.e sharedViewModel should not be used here
            lambdaMarkCompleted.invoke().markCompleted(taskList[position])
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}