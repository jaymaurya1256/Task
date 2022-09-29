package com.example.task.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NotNull val task: String,
    @NotNull val isActive: Boolean
)
