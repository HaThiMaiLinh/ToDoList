package com.example.todolist.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todolist.databinding.DialogConfirmBinding
import java.util.Objects


class DialogConfirm(var onClickConfirmTask: OnClickConfirmTask) : DialogFragment() {
    private lateinit var viewBinding: DialogConfirmBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DialogConfirmBinding.inflate(getLayoutInflater())
        setCancelable(false)
        return viewBinding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(
            Objects.requireNonNull(
                dialog
            )
                ?.window
        )
            ?.decorView!!.background = ColorDrawable(Color.TRANSPARENT)
        viewBinding.btnCancel.setOnClickListener {  dismiss() }
        viewBinding.btnSuccess.setOnClickListener { onClickConfirmTask.onClickConfirmDoneTaskSuccess() }
        dialog!!.window!!.decorView.background = ColorDrawable(Color.TRANSPARENT)
    }

    interface OnClickConfirmTask {
        fun onClickConfirmDoneTaskSuccess()
    }
}

