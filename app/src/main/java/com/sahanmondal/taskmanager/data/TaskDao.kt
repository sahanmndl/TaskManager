package com.sahanmondal.taskmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sort: SortOrder, hide: Boolean): Flow<List<Task>> =
        when (sort) {
            SortOrder.BY_DATE -> getTasksSortedByDate(query, hide)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hide)
        }

    @Query("SELECT * FROM tasks_table WHERE (completed != :hideCompletedTasks OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY prioritise DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE (completed != :hideCompletedTasks OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY prioritise DESC, timestamp")
    fun getTasksSortedByDate(searchQuery: String, hideCompletedTasks: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}