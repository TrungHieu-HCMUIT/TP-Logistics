package com.trunnghieu.tplogisticsapplication.ui.screens.phone_verify

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface PhoneVerifyUV : BaseUserView {
    fun onFragmentBackPressed()
    fun clearOtpCode()
    fun updatePhoneNumberSuccess()
}