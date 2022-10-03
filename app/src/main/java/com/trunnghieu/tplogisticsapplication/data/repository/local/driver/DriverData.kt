package com.trunnghieu.tplogisticsapplication.data.repository.local.driver

import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

data class DriverData(
    var fullName: String? = AppPreferences.get().getString(AppPrefs.DRIVER.FULL_NAME),
    var phoneNumber: String? = AppPreferences.get().getString(AppPrefs.DRIVER.MOBILE_NO),
    var avatarUrl: String? = AppPreferences.get().getString(AppPrefs.DRIVER.AVATAR_URL),
    var vehicleNumber: String? = AppPreferences.get().getString(AppPrefs.DRIVER.VEHICLE_NUMBER),
    var jobStatus: String? = AppPreferences.get().getString(AppPrefs.DRIVER.JOB_STATUS),
    var bookingNo: String? = AppPreferences.get().getString(AppPrefs.DRIVER.BOOKING_NO),
    var orgCode: String? = AppPreferences.get().getString(AppPrefs.DRIVER.ORG_CODE),
    var loadNo: Int? = AppPreferences.get().getInt(AppPrefs.DRIVER.LOAD_NO)
)