package com.example.task.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.ClickType
import com.example.task.models.TaskViewModel


class CompletedAdapter(private val task: List<Task>, private val onClick: (Task, ClickType, View) -> Unit)
    : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    class CompletedViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.listItemTextField)
        val taskList: ConstraintLayout = view.findViewById(R.id.list_item)
        val radioButton: RadioButton = view.findViewById(R.id.listItemRadioButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list ,parent,false)
        return CompletedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        holder.textView.text = task[position].task
        holder.radioButton.setOnClickListener{
            onClick(task[holder.adapterPosition],ClickType.SHORT,it)
        }

        holder.taskList.setOnLongClickListener {
            onClick(task[holder.adapterPosition], ClickType.LONG,it)
            true
        }
    }

    override fun getItemCount(): Int {
        return task.size
    }

}