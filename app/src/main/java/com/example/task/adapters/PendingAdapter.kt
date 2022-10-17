package com.example.task.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.MainActivity
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.TaskViewModel

class PendingAdapter(private val taskList: List<Task>,private val lambdaViewModel: ()->TaskViewModel) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
        val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)
        val radioButton: RadioButton = itemView.findViewById(R.id.listItemRadioButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.textView.text = taskList[position].task
        holder.radioButton.setOnClickListener {
            //lambda implementation should be there i.e sharedViewModel should not be used here
            lambdaViewModel.invoke().markCompleted(taskList[holder.adapterPosition])
        }
        holder.listItem.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                val popupMenu: PopupMenu = PopupMenu(p0?.context,p0)
                popupMenu.menuInflater.inflate(R.menu.item_list_menu,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener (
                    PopupMenu.OnMenuItemClickListener { it ->
                        when (it.itemId) {
                            R.id.delete -> {
                                lambdaViewModel.invoke().deleteTask(taskList[holder.adapterPosition])
                                return@OnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                lambdaViewModel.invoke().editTask(taskList[holder.adapterPosition],"Hello")
                                return@OnMenuItemClickListener true
                            }
                            else -> return@OnMenuItemClickListener true
                        }
                    }
                )

                popupMenu.show()
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}