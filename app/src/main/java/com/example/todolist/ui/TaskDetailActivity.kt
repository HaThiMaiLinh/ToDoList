package com.example.todolist.ui

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.todolist.R
import com.example.todolist.base.Application
import com.example.todolist.base.BaseActivity
import com.example.todolist.databinding.ActivityTaskDetailBinding
import com.example.todolist.dialog.DialogConfirm
import com.example.todolist.dialog.DialogUpdate
import com.example.todolist.model.TaskModel
import com.example.todolist.utils.Format
import com.example.todolist.viewModel.TaskViewModel
import com.example.todolist.viewModel.TaskViewModelFactory

class TaskDetailActivity : BaseActivity<ActivityTaskDetailBinding>(),
    DialogConfirm.OnClickConfirmTask,
    DialogUpdate.OnClickUpdateTask {
    override fun setViewBinding(): ActivityTaskDetailBinding {
        return ActivityTaskDetailBinding.inflate(layoutInflater)
    }

    private lateinit var taskModel: TaskModel
    private val viewModel : TaskViewModel by viewModels{
        TaskViewModelFactory((application as Application).repository)
    }
    var dialogUpdate: DialogUpdate? = null
    var dialogConfirm: DialogConfirm? = null
    override fun initView() {

        binding.apply {
            titleNameTask.text = taskModel.title
            desTask.text = taskModel.des
            nameTask.text = taskModel.title
            timeTask.text = Format.formatDateTimeToString(taskModel.time!! , taskModel.date!!)
            noteTask.setText(taskModel.note)
        }
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener { 
            finish()
        }
        binding.btnSuccess.setOnClickListener {
            if (binding.noteTask.getText().toString().isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.toast),
                    Toast.LENGTH_LONG
                ).show()
            } else if (!dialogConfirm!!.isAdded
                && !dialogConfirm!!.isVisible
                && !dialogConfirm!!.isRemoving
            ) {
                dialogConfirm!!.show(this.supportFragmentManager, javaClass.getName())
            }
        }
        binding.btnUpdate.setOnClickListener {
            if (!dialogUpdate!!.isAdded
                && !dialogUpdate!!.isVisible
                && !dialogUpdate!!.isRemoving
            ) {
                dialogUpdate!!.show(this.supportFragmentManager, javaClass.getName(), taskModel)
            }
        }
    }

    override fun initData() {
        if (intent.hasExtra("Task")){
            taskModel = intent.getSerializableExtra("Task") as TaskModel
        }
        dialogUpdate = DialogUpdate(this, this)
        dialogConfirm = DialogConfirm(this)
        setUpSpinner()
    }

    private fun setUpSpinner() {
        val score = ArrayAdapter<Int>(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            ArrayList<Int>(mutableListOf<Int>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        )
        score.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerScore.setAdapter(score)
    }

    override fun onClickConfirmDoneTaskSuccess() {
        val a = binding.spinnerScore.getSelectedItem().toString()

        taskModel.score = a
        taskModel.done = 1
        taskModel.note = binding.noteTask.getText().toString()
        viewModel.update(taskModel)

        dialogConfirm!!.dismiss()

        Toast.makeText(
            this,getString(R.string.label_da_cap_nhat),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    override fun onClickUpdateTaskSuccess(task: TaskModel) {
        viewModel.update(task)
        dialogUpdate!!.dismiss()
        Toast.makeText(
            this,getString(R.string.label_da_cap_nhat),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

}