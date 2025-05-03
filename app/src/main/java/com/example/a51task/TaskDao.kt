package com.example.a51task

import androidx.room.*

/**
 * 用于操作任务表的DAO接口，包含增删查改方法。
 */
@Dao
interface TaskDao {
    /**
     * 插入一条任务数据。
     * @param task 要插入的任务对象
     */
    @Insert
    suspend fun insertTask(task: Task)

    /**
     * 删除一条任务数据。
     * @param task 要删除的任务对象
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * 更新一条任务数据。
     * @param task 要更新的任务对象
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * 查询所有任务，按时间倒序排列。
     * @return 任务列表
     */
    @Query("SELECT * FROM task ORDER BY timestamp DESC")
    suspend fun getAllTasks(): List<Task>
}