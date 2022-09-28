package com.example.task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun TaskDao(): TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getDataBaseInstance(context: Context): TaskDatabase
        {
            if(INSTANCE == null)
            {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        TaskDatabase::class.java,
                        "taskDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}