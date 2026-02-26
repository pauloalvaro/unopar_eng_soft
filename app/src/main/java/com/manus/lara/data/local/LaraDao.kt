package com.manus.lara.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LaraDao {
    // Tasks
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY isCompleted ASC, effortLevel DESC")
    fun getTasksByDate(date: Long): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE date = :date AND isCompleted = 0")
    suspend fun deleteUnfinishedTasks(date: Long)

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY date DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    // House Config
    @Query("SELECT * FROM house_config WHERE id = 1")
    suspend fun getHouseConfig(): HouseConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHouseConfig(config: HouseConfigEntity)

    // Daily Log
    @Query("SELECT * FROM daily_log ORDER BY date DESC")
    fun getDailyLogs(): Flow<List<DailyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyLogEntity)
}
