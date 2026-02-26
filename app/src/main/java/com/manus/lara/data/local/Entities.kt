package com.manus.lara.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val isCompleted: Boolean = false,
    val date: Long, // Timestamp
    val category: String, // Diária, Semanal, Mensal
    val effortLevel: Int // 1-3
)

@Entity(tableName = "daily_log")
data class DailyLogEntity(
    @PrimaryKey val date: Long,
    val completedCount: Int,
    val totalCount: Int,
    val mood: String?
)

@Entity(tableName = "house_config")
data class HouseConfigEntity(
    @PrimaryKey val id: Int = 1,
    val rooms: String, // JSON string
    val hasKids: Boolean,
    val hasPets: Boolean,
    val appliances: String // JSON string
)
