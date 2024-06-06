package com.example.todolist.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.model.TaskModel
import com.example.todolist.model.dao.TaskDao

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class DataBaseApp : RoomDatabase() {

    abstract fun TaskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: DataBaseApp? = null

        fun getDatabase(
            context: Context
        ): DataBaseApp {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseApp::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}