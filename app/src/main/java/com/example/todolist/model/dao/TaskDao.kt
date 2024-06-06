package com.example.todolist.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.model.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<TaskModel>>

    @Query("select * from task where id = :id")
    fun getItem(id: Int): TaskModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskModel: TaskModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

}