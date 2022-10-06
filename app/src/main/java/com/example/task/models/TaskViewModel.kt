package com.example.task.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.TaskApplication
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.database.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {

    fun insert(task : String){
        viewModelScope.launch {
            DBHolder.db.taskDao().addTask(Task(0,task,true))
        }
    }
    suspend fun getDataPending() :LiveData<List<Task>>{
        lateinit var task:LiveData<List<Task>>
        viewModelScope.launch {
            task =  DBHolder.db.taskDao().getAllPending()
        }.join()
        return task
    }
    suspend fun getDataCompleted() :LiveData<List<Task>>{
        lateinit var task:LiveData<List<Task>>
        viewModelScope.launch {
            task =  DBHolder.db.taskDao().getAllCompleted()
        }.join()
        return task
    }

}