package com.example.task.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val task: String,
    val isActive: Boolean,
    val time: Long,
    val priority: String
)
