package com.example.todolist.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.todolist.databinding.DialogUpdateBinding
import com.example.todolist.model.TaskModel
import com.example.todolist.utils.Format
import java.time.LocalDateTime
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Objects


class DialogUpdate(context: Context?, var onClickUpdateTask: OnClickUpdateTask) :
    DialogFragment() {
    private lateinit var viewBinding: DialogUpdateBinding
    private lateinit var task: TaskModel
    var localDateTime: LocalDateTime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DialogUpdateBinding.inflate(getLayoutInflater())
        setCancelable(false)
        return viewBinding.getRoot()
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(
            Objects
                .requireNonNull(dialog)
                ?.window
        )!!.decorView.background = ColorDrawable(Color.TRANSPARENT)
        viewBinding.btnBack.setOnClickListener {
            dismiss()
            onDestroy()
        }
        viewBinding.btnUpdate.setOnClickListener {
            task.des = viewBinding.edtDesTask.getText().toString()
            task.time = Format.formattedTime(
                localDateTime!!.hour,
                localDateTime!!.minute
            )
            task.date = Format.formatDate(localDateTime)
            task.title = viewBinding.edtTask.getText().toString()

            onClickUpdateTask.onClickUpdateTaskSuccess(task)
        }
        timeDatePick()
        setUpView()
    }

    private fun setUpView() {
        viewBinding.setData(task)
    }

    fun show(manager: FragmentManager, tag: String?, task: TaskModel) {
        super.show(manager, tag)
        this.task = task
        localDateTime = Format.formatDateTimeToDate(task.time!!, task.date!!)
    }

    @SuppressLint("NewApi")
    fun timeDatePick() {
        viewBinding.tvTime.setOnClickListener {
            TimePickerDialog(
                context,
                OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    // Xử lý giờ và phút ở đây
                    localDateTime = localDateTime!!.withHour(hourOfDay).withMinute(minute)
                    val formattedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hourOfDay,
                        minute
                    )
                    viewBinding.tvTime.setText(formattedTime)
                },
                localDateTime!!.hour,
                localDateTime!!.minute,
                true
            ).show()
        }
        viewBinding.tvDate.setOnClickListener { v1 ->
            DatePickerDialog(
                requireContext(),
                OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val c =
                        GregorianCalendar(year, month, dayOfMonth)
                    viewBinding.tvDate.setText(Format.sdf(c.time))
                    localDateTime =
                        localDateTime!!.withYear(year).withMonth(month + 1)
                            .withDayOfMonth(dayOfMonth)
                },
                localDateTime!!.year,
                localDateTime!!.monthValue,
                localDateTime!!.dayOfMonth
            )
                .show()
        }
    }

    override fun dismiss() {
        super.dismiss()
        onDestroy()
    }

    interface OnClickUpdateTask {
        fun onClickUpdateTaskSuccess(task: TaskModel)
    }
}
