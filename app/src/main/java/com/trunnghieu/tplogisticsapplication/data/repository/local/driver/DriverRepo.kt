package com.trunnghieu.tplogisticsapplication.data.repository.local.driver

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

class DriverRepo {

    companion object {

        @Volatile
        private var INSTANCE: DriverRepo? = null

        @JvmStatic
        fun get(): DriverRepo = INSTANCE ?: synchronized(this) {
            val newInstance = INSTANCE ?: DriverRepo().also {
                INSTANCE = it
            }
            newInstance
        }
    }

    // Preferences
    private val appPrefs = AppPreferences.get()

    private var driverData: DriverData? = null
    val latestJobStatus = MutableLiveData(
        ApiConst.JobStatus.valueOf(
            appPrefs.getString(AppPrefs.DRIVER.JOB_STATUS).ifEmpty {
                ApiConst.JobStatus.DRIVER_JOB_STARTED.name
            }
        )
    )

    /**
     * Update Driver data from preferences after login / vehicle pairing
     */
    fun initDriverData() {
        driverData = DriverData(
            appPrefs.getString(AppPrefs.DRIVER.FULL_NAME),
            appPrefs.getString(AppPrefs.DRIVER.MOBILE_NO),
            appPrefs.getString(AppPrefs.DRIVER.AVATAR_URL),
            appPrefs.getString(AppPrefs.DRIVER.VEHICLE_NUMBER),
            appPrefs.getString(AppPrefs.DRIVER.JOB_STATUS),
            appPrefs.getString(AppPrefs.DRIVER.BOOKING_NO),
            appPrefs.getString(AppPrefs.DRIVER.ORG_CODE),
            appPrefs.getInt(AppPrefs.DRIVER.LOAD_NO),
        )
    }

    /**
     * Update vehicle number to preferences
     */
    fun updateVehicleNumber(truckNo: String) {
        appPrefs.storeValue(AppPrefs.DRIVER.VEHICLE_NUMBER, truckNo)
        initDriverData()
    }

    /**
     * Update latest job status to preferences
     */
    fun updateLatestJobStatus(jobStatus: String) {
        appPrefs.storeValue(AppPrefs.DRIVER.JOB_STATUS, jobStatus)
        latestJobStatus.value = ApiConst.JobStatus.valueOf(jobStatus)
        initDriverData()
    }

    /**
     * Update latest driver avatar url
     */
    fun updateDriverAvatar(avatarUrl: String) {
        appPrefs.storeValue(AppPrefs.DRIVER.AVATAR_URL, avatarUrl)
        initDriverData()
    }

    /**
     * Get driver data from local
     */
    fun getDriverData(): DriverData {
        if (driverData != null) return driverData!!
        initDriverData()
        return driverData!!
    }

    /**
     * Release data after logout / closed app to reduce memory
     */
    fun releaseData() {
        driverData = null
    }
}