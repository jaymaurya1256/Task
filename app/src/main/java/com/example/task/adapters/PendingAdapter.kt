package com.example.task.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.task.Color.ArtificialColors
import com.example.task.R
import com.example.task.database.Task
import com.example.task.models.ClickType
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log
import kotlin.math.min

private const val TAG = "PendingAdapter"
class PendingAdapter(private val taskList: List<Task>, private val onClick: (Task, ClickType)  -> Unit) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>(){
    class PendingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.listItemTextField)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)
        val radioButton: CheckBox = itemView.findViewById(R.id.checkBox_complete)
        val alarmTimeDisplay: TextView = itemView.findViewById(R.id.alarmTimeDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list,parent,false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.textView.text = taskList[position].task
        val context = holder.cardView.context
        when(taskList[position].priority){
            "LOW" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_green))
            "MEDIUM" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_orange))
            "HIGH" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        if (taskList[position].time != 0L) {
            Log.d(TAG, "onBindViewHolder: Going to convert time in millis to time in hour")
            val hour = SimpleDateFormat("hh", Locale.getDefault()).format(taskList[position].time).toLong()
            Log.d(TAG, "onBindViewHolder: Hour = $hour")
            val minute = SimpleDateFormat("mm", Locale.getDefault()).format(taskList[position].time).toLong()
            Log.d(TAG, "onBindViewHolder: Minutes = $minute")
            val amPm = SimpleDateFormat("a", Locale.getDefault()).format(taskList[position].time)
            Log.d(TAG, "onBindViewHolder: ampm = $amPm")
            val string = "${hour}:${minute} $amPm"
            Log.d(TAG, "onBindViewHolder: String $string")
            holder.alarmTimeDisplay.text = string
            Log.d(TAG, "onBindViewHolder: Textset on alarmtimedisplay = $string")
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
                        onClick(taskList[position], ClickType.LONG_DELETE)
                        true
                    }
                    R.id.edit -> {
                        onClick(taskList[position], ClickType.LONG_EDIT)
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