package com.example.task.models

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.task.AlarmReceiver
import com.example.task.MainActivity
import com.example.task.TaskApplication
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.database.TaskDatabase
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Nullable
import java.util.*

private const val TAG = "TaskViewModel"

class TaskViewModel(private val app: Application) : AndroidViewModel(app) {

    val pendingTask = DBHolder.db.taskDao().getAllPending()
    val completedTask = DBHolder.db.taskDao().getAllCompleted()

    fun markCompleted(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = false))
        }
    }

    fun markIncomplete(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = true))
        }
    }

    fun editTask(task: Task, newTask: String) {
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(task = newTask))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().removeTask(task)
        }
    }

    private fun setAlarm(calendar: Calendar, task: String) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            action = task
        }
        val pendingIntent = PendingIntent.getBroadcast(app, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val triggerTime = calendar.timeInMillis
        Log.d(TAG, "setAlarm: $triggerTime")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    fun insertTask(taskDescription: String, hour: Long, minute: Long, priority: Priority) {
        if (hour == -1L || minute == -1L) {
            viewModelScope.launch {
                DBHolder.db.taskDao().addTask(
                    Task(
                        id = 0,
                        task = taskDescription,
                        priority = priority.name,
                        isActive = true,
                        time = 0L
                    )
                )
            }
        } else {
            viewModelScope.launch {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
                calendar.set(Calendar.MINUTE, minute.toInt())
                calendar.set(Calendar.SECOND, 0)
                DBHolder.db.taskDao().addTask(
                    Task(
                        id = 0,
                        task = taskDescription,
                        priority = priority.name,
                        isActive = true,
                        time = calendar.timeInMillis
                    )
                )
                setAlarm(calendar, taskDescription)
            }
        }
    }
}