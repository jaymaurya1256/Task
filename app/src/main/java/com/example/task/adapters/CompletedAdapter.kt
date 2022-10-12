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
            onClick(task[position],ClickType.SHORT,it)
        }

        holder.taskList.setOnLongClickListener {
            onClick(task[position], ClickType.LONG,it)
            true
        }
        //Implement delete through this one
//        holder.taskList.setOnLongClickListener(object : View.OnLongClickListener{
//            override fun onLongClick(p0: View?): Boolean {
//                val popupMenu: PopupMenu = PopupMenu(p0?.context,p0)
//                popupMenu.menuInflater.inflate(R.menu.item_list_menu,popupMenu.menu)
//                popupMenu.setOnMenuItemClickListener (object : MenuItem.OnMenuItemClickListener{
//                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
//                        when(p0?.itemId){
//                            R.id.edit -> {
//                                Toast.makeText(holder.taskList.context,
//                                "Can't edit when task is already completed",
//                                Toast.LENGTH_LONG).show()
//                                return true
//                            }
//                            R.id.delete -> {
//                                onClick.invoke().deleteTask(task[holder.adapterPosition])
//                                return true
//                            }
//                        }
//                        return true
//                    }
//                })
//                popupMenu.show()
//            }
//        })
    }

    override fun getItemCount(): Int {
        return task.size
    }

}