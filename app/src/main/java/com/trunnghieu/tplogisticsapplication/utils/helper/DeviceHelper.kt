package com.trunnghieu.tplogisticsapplication.utils.helper

import android.content.Context
import android.content.pm.PackageManager
import com.trunnghieu.tplogisticsapplication.TPLogisticsApp
import java.util.*

object DeviceHelper {

    private var uniqueId: String? = null
    private val PREF_UNIQUE_ID: String = "PREF_UNIQUE_ID"

    /**
     * Generate unique Device ID
     */
    fun getDeviceId(): String {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
//        val m_szDevIDShort = "35" +
//                Build.BOARD.length % 10 +
//                Build.BRAND.length % 10 +
//                Build.CPU_ABI.length % 10 +
//                Build.DEVICE.length % 10 +
//                Build.MANUFACTURER.length % 10 +
//                Build.MODEL.length % 10 +
//                Build.PRODUCT.length % 10
//
//        // Thanks to @Roman SL!
//        // https://stackoverflow.com/a/4789483/950427
//        // Only devices with API >= 9 have android.os.Build.SERIAL
//        // http://developer.android.com/reference/android/os/Build.html#SERIAL
//        // If a user upgrades software or roots their device, there will be a duplicate entry
//        var serial: String
//        try {
//            serial = Build::class.java.getField("SERIAL")[null].toString()
//
//            // Go ahead and return the serial for api => 9
//            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
//        } catch (exception: Exception) {
//            // String needs to be initialized
//            serial = "serial" // some value
//        }
//
//        // Thanks @Joe!
//        // https://stackoverflow.com/a/2853253/950427
//        // Finally, combine the values we have found by using the UUID class to create a unique identifier
//        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()

        if (uniqueId == null) {
            val preferences =
                TPLogisticsApp.appContext.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
            uniqueId = preferences.getString(PREF_UNIQUE_ID, null)
            if (uniqueId == null) {
                uniqueId = UUID.randomUUID().toString()
                val editor = preferences.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueId)
                editor.apply()
            }
        }
        return uniqueId!!
    }

    /**
     * Get app version
     */
    fun getAppVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager
                .getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "1.0.0"
        } catch (e: NumberFormatException) {
            "1.0.0"
        }
    }
}