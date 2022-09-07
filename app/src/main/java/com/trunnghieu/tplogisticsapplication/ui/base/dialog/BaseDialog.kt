package com.trunnghieu.tplogisticsapplication.ui.base.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

abstract class BaseDialog {
    protected var context: Context? = null
    protected var dialog: AlertDialog? = null

    /**
     * Init context
     */
    fun init(appContext: Context) {
        context = appContext
    }

    /**
     * Dismiss dialog
     */
    fun dismiss() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}