package com.example.task.models

import android.app.Application
import androidx.lifecycle.*
import com.example.task.TaskApplication
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.database.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {

    val pendingTask = DBHolder.db.taskDao().getAllPending()
    val completedTask = DBHolder.db.taskDao().getAllCompleted()
    fun insert(task : String){
        viewModelScope.launch {
            DBHolder.db.taskDao().addTask(Task(0,task,true))
        }
    }
    fun markCompleted(task: Task){
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = false))
        }
    }
    fun markIncomplete(task: Task){
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(isActive = true))
        }
    }
    fun editTask(task: Task, newTask: String){
        viewModelScope.launch {
            DBHolder.db.taskDao().updateTask(task.copy(task = newTask))
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            DBHolder.db.taskDao().removeTask(task)
        }
    }
}