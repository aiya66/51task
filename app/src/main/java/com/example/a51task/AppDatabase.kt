package com.example.a51task

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room数据库主类，包含任务表和对应的DAO。
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * 获取任务表的DAO接口
     */
    abstract fun taskDao(): TaskDao

    companion object {
        // 单例，保证全局只有一个数据库实例
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 获取数据库实例
         * @param context 上下文
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}