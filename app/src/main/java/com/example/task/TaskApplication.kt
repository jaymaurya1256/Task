package com.example.task

import android.app.Application
import androidx.room.Room
import com.example.task.database.DBHolder
import com.example.task.database.TaskDatabase

class TaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DBHolder.db = Room.databaseBuilder(applicationContext,TaskDatabase::class.java,"task_db").build()
    }
}

