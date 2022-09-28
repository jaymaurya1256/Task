package com.example.task.models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.task.database.Task
import com.example.task.database.TaskDatabase

class TaskViewModel: ViewModel() {
    private var _database:TaskDatabase = TaskDatabase.getDataBaseInstance()
    val database get() = _database
}