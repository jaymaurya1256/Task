package com.example.task.models

import androidx.lifecycle.ViewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.database.Task
import com.example.task.database.TaskApplication
import com.example.task.database.TaskDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: TaskApplication): AndroidViewModel(application) {
    val database: TaskDatabase = TaskDatabase.getDataBaseInstance(getApplication<Application>().applicationContext)

    fun insert(id:Int,task:String,isActive:Int)
    {
        viewModelScope.launch {
            database.TaskDao().addTask(Task(id,task,isActive))
        }
    }
    fun update(id:Int,task:String,isActive:Int)
    {
        viewModelScope.launch {
            database.TaskDao().markComplete(Task(id,task,isActive))
        }
    }

}