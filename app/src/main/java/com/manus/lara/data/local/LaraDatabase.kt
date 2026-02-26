package com.manus.lara.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class, DailyLogEntity::class, HouseConfigEntity::class], version = 1, exportSchema = false)
abstract class LaraDatabase : RoomDatabase() {
    abstract fun laraDao(): LaraDao

    companion object {
        @Volatile
        private var INSTANCE: LaraDatabase? = null

        fun getDatabase(context: Context): LaraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LaraDatabase::class.java,
                    "lara_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
