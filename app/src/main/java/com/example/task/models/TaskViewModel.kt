package com.example.task.models

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.task.AlarmReceiver
import com.example.task.database.DBHolder
import com.example.task.database.Task
import kotlinx.coroutines.launch
import java.util.Calendar

private const val TAG = "TaskViewModel"

class TaskViewModel(private val app: Application) : AndroidViewModel(app) {

    val pendingTask = DBHolder.db.taskDao().getAllPending()
    val completedTask = DBHolder.db.taskDao().getAllCompleted()
    val task = MutableLiveData<Task>()

    @RequiresApi(Build.VERSION_CODES.M)
    fun markCompleted(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = false))
            removeAlarm(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun markIncomplete(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = true))
            setAlarm(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun editTask(id:Int, newTaskDescription: String, hour: Long, minute: Long, priority: Priority) {
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
            removeAlarm(task)
            setAlarm(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            if(task.time > Calendar.getInstance().timeInMillis){
                removeAlarm(task)
            }
            DBHolder.db.taskDao().removeTask(task)
        }
    }

    fun deleteAllFromPending(){
        viewModelScope.launch {
            DBHolder.db.taskDao().deleteAllFromPending()
        }
    }
    fun deleteAllFromCompleted(){
        viewModelScope.launch {
            DBHolder.db.taskDao().deleteAllFromCompleted()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun removeAlarm(task: Task) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            action = task.task
        }
        val pendingIntent = PendingIntent.getBroadcast(
            app,
            task.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(task: Task) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java).apply {
            action = task.task
        }
        val pendingIntent = PendingIntent.getBroadcast(app, task.id, intent, PendingIntent.FLAG_IMMUTABLE)
        val triggerTime = task.time
        Log.d(TAG, "setAlarm: $triggerTime")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                val settingIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:${app.packageName}")
                }
                startActivity(app,settingIntent, null)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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