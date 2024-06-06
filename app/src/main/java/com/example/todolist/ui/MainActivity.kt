package com.example.todolist.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.adapter.TaskClick
import com.example.todolist.base.Application
import com.example.todolist.base.BaseActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogAddTaskBinding
import com.example.todolist.dialog.DialogTaskEnd
import com.example.todolist.model.TaskModel
import com.example.todolist.utils.Format
import com.example.todolist.viewModel.TaskViewModel
import com.example.todolist.viewModel.TaskViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class MainActivity : BaseActivity<ActivityMainBinding>(),TaskClick {
    override fun setViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val dialogTaskEnd : DialogTaskEnd by lazy {
        DialogTaskEnd()
    }
    private val viewModel : TaskViewModel by viewModels{
        TaskViewModelFactory((application as Application).repository)
    }
    private val adapter : TaskAdapter by lazy {
        TaskAdapter(this,this)
    }
    private var listTask : List<TaskModel> ?=null
    override fun initView() {
        binding.searchTask.clearFocus()
        binding.searchTask.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                FinterList(newText)
                return true
            }
        })
    }

    override fun initListener() {
        binding.imgAddTask.setOnClickListener { v ->
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bindingDialog: DialogAddTaskBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_add_task,
                null,
                false
            )
            dialog.setContentView(bindingDialog.getRoot())
            val window = dialog.window ?: return@setOnClickListener
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val windowacc = window.attributes
            windowacc.gravity = Gravity.NO_GRAVITY
            window.setAttributes(windowacc)
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val sdf1 = SimpleDateFormat("HH:mm")
            bindingDialog.tvTime.setOnClickListener(View.OnClickListener {
                val timePickerDialog = TimePickerDialog(
                    this,
                    { view, hourOfDay, minute ->
                        val formattedTime = String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hourOfDay,
                            minute
                        )
                        bindingDialog.tvTime.setText(formattedTime)
                    },
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE],
                    true
                )
                timePickerDialog.show()
            })
            bindingDialog.tvDate.setOnClickListener { v1 ->
                val dialog1 = DatePickerDialog(
                    this,
                    { view, year, month, dayOfMonth ->
                        val c =
                            GregorianCalendar(year, month, dayOfMonth)
                        bindingDialog.tvDate.setText(sdf.format(c.time))
                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DATE]
                )
                dialog1.show()
            }
            bindingDialog.imgCancel.setOnClickListener { v12 -> dialog.cancel() }
            bindingDialog.btnAddTask.setOnClickListener { v13 ->
                if (bindingDialog.tvTime.getText().toString().trim().isEmpty()) {
                    bindingDialog.tvTime.setError("Select time to limit")
                }
                if (bindingDialog.tvDate.getText().toString().trim().isEmpty()) {
                    bindingDialog.tvDate.setError("select the date to limit")
                }
                if (bindingDialog.edtTask.getText().toString().trim().isEmpty()) {
                    bindingDialog.edtTask.setError("You have not entered a task yet")
                }
                if (bindingDialog.edtDesTask.getText().toString().trim().isEmpty()) {
                    bindingDialog.edtDesTask.setError("You have not entered a description yet")
                } else {
                    val task = TaskModel(bindingDialog.edtTask.getText().toString().trim(),
                        bindingDialog.edtDesTask.getText().toString().trim(),
                        "",bindingDialog.tvTime.getText().toString().trim(),
                        bindingDialog.tvDate.getText().toString().trim(),0,0,"0/10")

                    viewModel.insert(task)
                    Toast.makeText(this, "Added task successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        registerForContextMenu(binding.imgFilter)
    }

    override fun initData() {
        viewModel.getAll.observe(this){
            binding.apply {
                adapter.setItems(it)
                listTask = it as ArrayList<TaskModel>?
                rcvTasks.adapter = adapter
                rcvTasks.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun FinterList(text: String) {
        val filteredList: ArrayList<TaskModel> = ArrayList<TaskModel>()
        for (task in listTask!!) {
            if (task.title!!.toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
                filteredList.add(task)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
        } else {
            adapter.setItems(filteredList)
        }
    }

    override fun onLongClick(item: TaskModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to delete ?")
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface?, i: Int ->
            viewModel.delete(item)
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(
            "Cancle"
        ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
        builder.show()
    }

    @SuppressLint("NewApi")
    override fun onClick(item: TaskModel) {
        if (Format.formatDateTimeToDate(item.time!!, item.date!!)
                .isAfter(LocalDateTime.now()) && item.done == 0
        ) {

            startActivity(Intent(this,TaskDetailActivity::class.java).putExtra("Task",item))

        } else {
            dialogTaskEnd.show(
                this.getSupportFragmentManager(),
                javaClass.getName(),
                item
            )
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = this.getMenuInflater()
        inflater.inflate(R.menu.contentmenu, menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_alll -> {
                adapter.setItems(listTask!!)
                true
            }

            R.id.menu_completed -> {
                val completedList: ArrayList<TaskModel> = ArrayList<TaskModel>()
                for (task in listTask!!) {
                    if (task.done == 1) {
                        completedList.add(task)
                    }
                }
                if (completedList.isEmpty()) {
                    Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setItems(completedList)
                }
                true
            }

            R.id.menu_not_completed -> {
                val notcompletedList: ArrayList<TaskModel> = ArrayList<TaskModel>()
                for (task in listTask!!) {
                    if (task.done == 0) {
                        notcompletedList.add(task)
                    }
                }
                if (notcompletedList.isEmpty()) {
                    Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setItems(notcompletedList)
                }
                true
            }

            R.id.menu_late -> {
                val lateList: ArrayList<TaskModel> = ArrayList<TaskModel>()
                for (task in listTask!!) {
                    val localCalendar = Calendar.getInstance()
                    val givenTime: String = task.date + " " + task.time
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
                    try {
                        val givenDate = dateFormat.parse(givenTime)
                        val givenCalendar = Calendar.getInstance()
                        givenCalendar.setTime(givenDate)
                        if (task.done == 0 && localCalendar.after(givenCalendar)) {
                            lateList.add(task)
                        }
                    } catch (e: ParseException) {
                        throw RuntimeException(e)
                    }
                    if (lateList.isEmpty()) {
                        Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.setItems(lateList)
                    }
                }
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

}