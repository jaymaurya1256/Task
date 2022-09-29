package com.example.task.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao  {
    @Query("SELECT * FROM Task WHERE isActive != 0 ORDER BY id")
    fun getAllPending(): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE isActive == 0 ORDER BY id")
    fun getAllCompleted(): LiveData<List<Task>>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun markComplete(task: Task)

    @Delete
    suspend fun removeTask(task: Task)
}