package com.example.task.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.ClickType
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "PendingAdapter"
class PendingAdapter(
    private val onClick: (Task, ClickType)  -> Unit
) : ListAdapter<Task, PendingAdapter.PendingViewHolder>(TaskDiff()) {
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
        val priority: ImageView = itemView.findViewById(R.id.priority)
        val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)
        val radioButton: CheckBox = itemView.findViewById(R.id.checkBox_complete)
        val alarmTimeDisplay: TextView = itemView.findViewById(R.id.alarmTimeDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        val task = getItem(position)
        holder.textView.text = task.task

        when(task.priority){
            "LOW" -> holder.priority.setBackgroundResource(R.drawable.green_dot)
            "MEDIUM" -> holder.priority.setBackgroundResource(R.drawable.yellow_dot)
            "HIGH" -> holder.priority.setBackgroundResource(R.drawable.red_dot)
        }

        Log.d(TAG, "Position: $position, task: $task")
        if (task.time != 0L) {
            val hour = SimpleDateFormat("hh", Locale.getDefault()).format(task.time).toLong()
            val minute = SimpleDateFormat("mm", Locale.getDefault()).format(task.time).toLong()
            val amPm = SimpleDateFormat("a", Locale.getDefault()).format(task.time)
            val string = "${hour}:${minute} $amPm"
            Log.d(TAG, "onBindViewHolder: Time $string for position $position")
            holder.alarmTimeDisplay.visibility = View.VISIBLE
            holder.alarmTimeDisplay.text = string
        } else {
            holder.alarmTimeDisplay.visibility = View.GONE
            Log.d(TAG, "onBindViewHolder: Hiding view at position $position, task item time: ${task.time}")
        }
        //Set the reminder time
        holder.radioButton.setOnClickListener {
            //lambda implementation should be there i.e sharedViewModel should not be used here
            onClick(task, ClickType.SHORT)
        }
        holder.listItem.setOnLongClickListener { p0 ->
            val popupMenu = PopupMenu(p0?.context, p0)
            popupMenu.menuInflater.inflate(R.menu.item_list_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { it ->
                when (it.itemId) {
                    R.id.delete -> {
                        onClick(task, ClickType.LONG_DELETE)
                        true
                    }
                    R.id.edit -> {
                        onClick(task, ClickType.LONG_EDIT)
                        true
                    }
                    else -> true
                }
            }

            popupMenu.show()
            true
        }
    }
}

class TaskDiff : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}