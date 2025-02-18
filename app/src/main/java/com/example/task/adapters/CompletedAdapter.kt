package com.example.task.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Color.ArtificialColors
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.ClickType

class CompletedAdapter(private val task: List<Task>, private val onClick: (Task, ClickType, View) -> Unit) : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {
    class CompletedViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.listItemTextField)
        val taskList: ConstraintLayout = view.findViewById(R.id.list_item)
        val cardView: CardView = view.findViewById(R.id.cardView)
        val radioButton: CheckBox = view.findViewById(R.id.checkBox_complete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list ,parent,false)
        return CompletedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        val myColor = ArtificialColors()
        holder.textView.text = task[position].task
        holder.textView.setTextColor(Color.WHITE)
        holder.cardView.setCardBackgroundColor(Color.DKGRAY)
        holder.radioButton.isChecked = true // Since this is the completed adapter, all the tasks will be checked
        holder.radioButton.setOnClickListener{
            onClick(task[holder.adapterPosition],ClickType.SHORT,it)
        }
        holder.taskList.setOnLongClickListener {
            val popupMenu: PopupMenu = PopupMenu(it.context,it)
            popupMenu.menuInflater.inflate(R.menu.item_list_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete -> {
                        onClick(task[holder.adapterPosition], ClickType.LONG_DELETE, holder.itemView)
                        true
                    }
                    R.id.edit -> {
                        Toast.makeText(holder.itemView.context,"Can't edit the task after completion",Toast.LENGTH_LONG).show()
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
        return task.size
    }

}