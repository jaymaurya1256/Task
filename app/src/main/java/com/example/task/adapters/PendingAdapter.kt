package com.example.task.adapters

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
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.models.ClickType
import com.example.task.models.TaskViewModel

class PendingAdapter(private val taskList: List<Task>, private val onClick: (Task, ClickType)  -> Unit) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
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
            onClick( taskList[position], ClickType.SHORT)
        }
        holder.listItem.setOnLongClickListener { p0 ->
            val popupMenu: PopupMenu = PopupMenu(p0?.context, p0)
            popupMenu.menuInflater.inflate(R.menu.item_list_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { it ->
                when (it.itemId) {
                    R.id.delete -> {
                        onClick( taskList[holder.adapterPosition], ClickType.LONG_DELETE )
                        true
                    }
                    R.id.edit -> {
                        onClick( taskList[holder.adapterPosition], ClickType.LONG_EDIT )
                        true
                    }
                    else -> true
                }
            }

            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}