package com.example.task.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.TaskApplication
import com.example.task.database.DBHolder
import com.example.task.database.Task
import com.example.task.database.TaskDatabase
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {

    fun insert(task : String){
        viewModelScope.launch {
            DBHolder.db.taskDao().addTask(Task(0,task,true))
        }
    }
}