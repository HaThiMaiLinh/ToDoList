package com.example.todolist.base

import android.app.Application
import com.example.todolist.dataBase.DataBaseApp
import com.example.todolist.repository.TaskRepository

class Application : Application() {

    val database by lazy { DataBaseApp.getDatabase(this) }
    val repository by lazy { TaskRepository(database.TaskDao()) }

    override fun onCreate() {
        super.onCreate()

    }

}

