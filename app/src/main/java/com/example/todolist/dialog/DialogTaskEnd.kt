package com.example.todolist.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.todolist.databinding.DialogTaskEndBinding
import com.example.todolist.model.TaskModel
import com.example.todolist.utils.Format
import java.util.Objects


class DialogTaskEnd : DialogFragment() {
    private lateinit var viewBinding: DialogTaskEndBinding
    private lateinit var task: TaskModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DialogTaskEndBinding.inflate(getLayoutInflater())
        setCancelable(false)
        return viewBinding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(
            Objects
                .requireNonNull(dialog)
                ?.window
        )?.decorView?.background = ColorDrawable(Color.TRANSPARENT)
        viewBinding.btnSuccess.setOnClickListener { dismiss() }
        setUpView()
    }

    private fun setUpView() {
        viewBinding.setTask(task)
        viewBinding.timeTask.setText(Format.formatDateTimeToString(task.time!!, task.date!!))
    }

    fun show(manager: FragmentManager, tag: String?, task: TaskModel) {
        super.show(manager, tag)
        this.task = task
    }
}
