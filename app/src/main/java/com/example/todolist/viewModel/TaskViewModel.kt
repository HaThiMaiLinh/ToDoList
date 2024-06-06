package com.example.todolist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.TaskModel
import com.example.todolist.repository.TaskRepository
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _getAll = MutableLiveData<List<TaskModel>>()
    val getAll: LiveData<List<TaskModel>> = _getAll


    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            repository.getAll.collect {
                _getAll.postValue(it)
            }
        }
    }

    fun getItem(id :Int) = viewModelScope.launch {
        repository.getItem(id)
    }

    fun insert(taskModel: TaskModel) = viewModelScope.launch {
        repository.insert(taskModel)
    }

    fun update(taskModel: TaskModel) = viewModelScope.launch {
        repository.update(taskModel)
    }

    fun delete(taskModel: TaskModel) = viewModelScope.launch {
        repository.delete(taskModel)
    }

}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}