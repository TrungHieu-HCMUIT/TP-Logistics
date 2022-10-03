package com.trunnghieu.tplogisticsapplication.data.repository.remote

import com.google.gson.annotations.SerializedName
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.DeviceHelper

open class BaseDTO {
    @SerializedName("deviceId")
    var deviceId: String = DeviceHelper.getDeviceId()

    @SerializedName("loginId")
    var loginId: String = AppPreferences.get().getString(AppPrefs.LOGIN.PHONE_NUMBER, "")
}