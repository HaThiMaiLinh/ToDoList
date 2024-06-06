package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.example.todolist.R
import com.example.todolist.base.BaseAdapter
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.model.TaskModel
import com.example.todolist.utils.Format
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class TaskAdapter(private val context: Context,private val listener : TaskClick) : BaseAdapter<TaskModel,ItemTaskBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemTaskBinding {
        return ItemTaskBinding.inflate(inflater,parent,false)
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun bind(binding: ItemTaskBinding, item: TaskModel, position: Int) {
        binding.apply {
            tvTitleTask.text = item.title
            tvDesTask.text = item.des
            tvTimeTask.text = item.time + "date" + item.date
            tvPoinTask.text = item.score


            if (item.done == 1) {
                ViewCompat.setBackgroundTintList(
                    statusTask,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                )
            } else if (item.done == 0
                && Format.formatDateTimeToDate(item.time!!, item.date!!)
                    .isBefore(LocalDateTime.now())
            ) {
                ViewCompat.setBackgroundTintList(
                    statusTask,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
                )
            } else if (item.done == 0
                && LocalDateTime.now().plusHours(6)
                    .until(
                        Format.formatDateTimeToDate(item.time!!, item.date!!),
                        ChronoUnit.HOURS
                    ) < 6
            ) {
                ViewCompat.setBackgroundTintList(
                    statusTask,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange))
                )
            }

            root.setOnClickListener {
                listener.onClick(item)
                notifyDataSetChanged()
            }

            root.setOnLongClickListener{
                listener.onLongClick(item)
                notifyDataSetChanged()
                true
            }
        }
    }
}


interface TaskClick {
    fun onLongClick(item: TaskModel)
    fun onClick(item: TaskModel)
}
