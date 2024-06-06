package com.example.todolist.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.todolist.model.TaskModel
import com.example.todolist.model.dao.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(private val dao: TaskDao) {
    val getAll : Flow<List<TaskModel>> = dao.getAll()

    @WorkerThread
    suspend fun getItem(id : Int) {
        withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }
    @WorkerThread
    suspend fun insert(taskModel: TaskModel) {
        withContext(Dispatchers.IO) {
            dao.insert(taskModel)
        }
    }
    @WorkerThread
    suspend fun update(taskModel: TaskModel) {
        withContext(Dispatchers.IO) {
            dao.update(taskModel)
        }
    }
    @WorkerThread
    suspend fun delete(taskModel: TaskModel) {
        withContext(Dispatchers.IO) {
            dao.delete(taskModel)
        }
    }
}