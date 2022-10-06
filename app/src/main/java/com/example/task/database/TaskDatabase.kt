package com.example.task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class  TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

}

object DBHolder {
    lateinit var db: TaskDatabase
}