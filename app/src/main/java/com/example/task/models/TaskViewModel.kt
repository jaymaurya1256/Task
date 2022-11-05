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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import androidx.room.Room
import com.example.task.AlarmReceiver
import com.example.task.MainActivity
import com.example.task.TaskApplication
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.database.TaskDatabase
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Nullable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

private const val TAG = "TaskViewModel"

class TaskViewModel(private val app: Application) : AndroidViewModel(app) {

    val pendingTask = DBHolder.db.taskDao().getAllPending()
    val completedTask = DBHolder.db.taskDao().getAllCompleted()
    val task = MutableLiveData<Task>()

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

    fun editTask(id:Int,newTaskDescription: String, hour: Long, minute: Long, priority: Priority) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY,hour.toInt())
            calendar.set(Calendar.MINUTE, minute.toInt())
            calendar.set(Calendar.SECOND,0)
            val task = Task(
                id = id,
                task = newTaskDescription,
                isActive = true,
                time = calendar.timeInMillis,
                priority = priority.name
            )

            DBHolder.db.taskDao().updateTask(task)
            setAlarm(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            removeAlarm(task)
            DBHolder.db.taskDao().removeTask(task)
        }
    }
    fun deleteAll(){
        TODO()
    }
    private fun removeAlarm(task: Task) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            action = task.task
        }
        val pendingIntent = PendingIntent.getBroadcast(app, task.id, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(task: Task) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            action = task.task
        }
        val pendingIntent = PendingIntent.getBroadcast(app, task.id, intent, 0)
        val triggerTime = task.time
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
                val task:Task = Task(
                    id = 0,
                    task = taskDescription,
                    priority = priority.name,
                    isActive = true,
                    time = calendar.timeInMillis)
                DBHolder.db.taskDao().addTask(task)
                setAlarm(task)
            }
        }
    }

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            task.value = DBHolder.db.taskDao().getTask(taskId)
        }
    }
}