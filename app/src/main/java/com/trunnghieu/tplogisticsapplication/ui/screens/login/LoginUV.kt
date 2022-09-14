package com.trunnghieu.tplogisticsapplication.ui.screens.login

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface LoginUV : BaseUserView {
//    fun askPermissions(permissions: List<String>)
//    fun askLocationPermissions(locationPermissions: List<String>)
//    fun goToOverlaySetting()
    fun goToResetPassword()
//    fun goToChangePassword()
    fun goToMain()
    fun resetValidate()
    fun phoneNumberAndPasswordIsEmpty()
    fun phoneNumberIsEmpty()
    fun passwordIsEmpty()
//    fun invalidLoginInfo()
//    fun connectionError()
    fun showLanguagePopup()
}