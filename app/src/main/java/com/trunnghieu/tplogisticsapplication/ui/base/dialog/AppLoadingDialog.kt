package com.trunnghieu.tplogisticsapplication.ui.base.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.DialogLoadingBinding
import com.trunnghieu.tplogisticsapplication.databinding.DialogProgressBinding

class AppLoadingDialog : BaseDialog() {

    companion object {

        @Volatile
        private var instance: AppLoadingDialog? = null

        fun get(): AppLoadingDialog =
            instance ?: synchronized(this) {
                val newInstance = instance ?: AppLoadingDialog()
                    .also { instance = it }
                newInstance
            }
    }

    private var progressDialogBinding: DialogProgressBinding? = null

    /**
     * Show loading dialog
     */
    fun show() {
        context?.let { context ->
            val binding: DialogLoadingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_loading,
                null, true
            )

            dialog = AlertDialog.Builder(context, R.style.Base_MaterialAlertDialog)
                .setView(binding.root)
                .setCancelable(false)
                .create()
                .also {
                    dismiss()

                    if ((context as Activity).isFinishing) return
                    it.show()

                    // Custom background
                    val window = it.window
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
        }
    }

    /**
     * Show progress dialog
     */
    fun showProgress(title: String? = null) {
        context?.let { context ->
            progressDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_progress,
                null, true
            )
            progressDialogBinding!!.title = title

            dialog = AlertDialog.Builder(context, R.style.Base_MaterialAlertDialog)
                .setView(progressDialogBinding!!.root)
                .setCancelable(false)
                .create()
                .also {
                    dismiss()

                    if ((context as Activity).isFinishing) return
                    it.show()

                    // Custom background
                    val window = it.window
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
        }
    }

    /**
     * Update progress
     */
    fun updateProgress(progress: Int, fileIndex: Int? = 1, totalFiles: Int? = 1) {
        progressDialogBinding?.apply {
            this.progress = progress
            this.fileIndex = fileIndex
            this.totalFiles = totalFiles
            executePendingBindings()
        }
    }
}