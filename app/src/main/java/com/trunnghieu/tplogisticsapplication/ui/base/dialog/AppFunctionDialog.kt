package com.trunnghieu.tplogisticsapplication.ui.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.trunnghieu.tplogisticsapplication.R

class AppFunctionDialog {

    companion object {

        @Volatile
        private var instance: AppFunctionDialog? = null

        fun get(): AppFunctionDialog =
            instance ?: synchronized(this) {
                val newInstance = instance ?: AppFunctionDialog()
                    .also { instance = it }
                newInstance
            }
    }

    private var dialog: AlertDialog? = null

    /**
     * Show dialog with custom layout
     */
    fun showCustomLayout(
        context: Context,
        layoutBinding: ViewDataBinding,
        percentWidth: Double = 1.0,
        gravity: Int = Gravity.CENTER
    ): AlertDialog {
        val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.Base_MaterialAlertDialog)
            .apply {
                setCancelable(false)

                // Custom View
                setView(layoutBinding.root)
            }
        if (dialog?.isShowing == true) dialog?.dismiss()
        val customDialog = dialogBuilder.show()

        // Custom background
        val window = customDialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Width - Height
        val displayRectangle = Rect()
        window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)
        customDialog.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                (percentWidth * displayRectangle.width()).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(gravity)
        }

        return customDialog
    }

    /**
     * Dismiss dialog
     */
    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}