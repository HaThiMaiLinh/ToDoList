package com.example.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class TaskModel (
    var title: String? = null,
    var des: String? = null,
    var note: String? = null,
    var time: String? = null,
    var date: String? = null,
    var done: Int = 0,
    var notified: Int = 0,
    var score: String? = null,
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}