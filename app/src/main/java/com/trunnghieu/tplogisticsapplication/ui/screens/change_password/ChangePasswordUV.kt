package com.trunnghieu.tplogisticsapplication.ui.screens.change_password

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface ChangePasswordUV : BaseUserView {
    fun onBackPressed()
    fun resetValidate()
    fun currentPasswordInCorrect()
    fun newPasswordNotMatch()
    fun newPasswordMustDifferentFromCurrentPassword()
    fun changePasswordSuccess(message: String = "")
}