package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task

class PendingAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.textView.text = taskList[position].task
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}