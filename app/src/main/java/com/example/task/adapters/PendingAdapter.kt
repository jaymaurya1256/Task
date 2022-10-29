package com.example.task.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Color.ArtificialColors
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.ClickType

class PendingAdapter(private val taskList: List<Task>, private val onClick: (Task, ClickType)  -> Unit) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)
        val radioButton: CheckBox = itemView.findViewById(R.id.checkBox_complete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.textView.text = taskList[position].task
        //Set the card color according to priority
        val myColors = ArtificialColors()
        when(taskList[position].priority){
            "LOW" -> holder.cardView.setCardBackgroundColor(myColors.GREEN_COLOR)
            "MEDIUM" -> holder.cardView.setCardBackgroundColor(myColors.YELLOW_COLOR)
            "HIGH" -> holder.cardView.setCardBackgroundColor(myColors.RED_COLOR)
        }
        //Set the reminder time
        holder.radioButton.setOnClickListener {
            //lambda implementation should be there i.e sharedViewModel should not be used here
            onClick( taskList[position], ClickType.SHORT)
        }
        holder.listItem.setOnLongClickListener { p0 ->
            val popupMenu = PopupMenu(p0?.context, p0)
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