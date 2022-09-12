package com.trunnghieu.tplogisticsapplication.ui.screens.reset_password

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface ResetPasswordUV : BaseUserView {
    fun onBackPressed()
    fun resetValidate()
    fun phoneNumberIsEmpty()
}