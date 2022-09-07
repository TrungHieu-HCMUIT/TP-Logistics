package com.trunnghieu.tplogisticsapplication.data.api

import com.trunnghieu.tplogisticsapplication.utils.constant.Const

object ApiConst {

    // Url
    private const val API_VERSION = 1
    private const val BASE_DEV_URL = "https://localhost:8080/"
    private const val BASE_PROD_URL = "https://localhost:8080/"

    val BASE_API_URL = (
            if (Const.DEBUG_MODE)
                BASE_DEV_URL
            else
                BASE_PROD_URL
            ) + "api/mobile/v$API_VERSION/"
    val BASE_WEB_URL = (
            if (Const.DEBUG_MODE)
                BASE_DEV_URL
            else
                BASE_PROD_URL
            ) + "mobile/"

    // Check update
    const val APP_PROTOCOL = "1.x"
    const val HTTP_CODE_UPGRADE_REQUIRED = 419

    // Notification
    const val PUSH_NOTIFICATION_TYPE = "2" // 1: iOS - 2: Android

    // Job Status
    enum class JobStatus {
        OPEN, // Go to Vehicle Pairing - With scan QR
        ASSIGNED, // Go to Vehicle Pairing - With Start Work button
        DRIVER_JOB_STARTED, // Go To Pickup Location
        DRIVER_PICKUP_ARRIVED, // Go To Pickup Material
        DRIVER_PICKUP_DONE, // Go To Delivery Location (TRIP BASED)
        DRIVER_PICKUP_TONNAGE_SUBMITTED, // Go To Delivery Location (TON BASED)
        DRIVER_DELIVERY_ARRIVED, // Go To Discharge Material (show Job Complete button) (TRIP BASED)
        DRIVER_DISCHARGE_TONNAGE_SUBMITTED, // Go To Discharge Material (TON BASED)
        DRIVER_DISCHARGED, // Go To Discharge Material (show Scan DO button)
        DRIVER_JOB_COMPLETED, // Go To Job Summary (DO scanned)
    }

    // For TON BASED
    const val TON_BASED_NET_WEIGHT = 17500.0

    // For E-Signature
    object ESign {
        const val FOR_PICKUP = "Pickup"
        const val FOR_DELIVERY = "Unload"
    }

    // Dispatcher - Receiver
    object JobType {
        const val JOB_DISPATCHER = "dispatcher"
        const val JOB_RECEIVER = "receiver"
    }

    object Error {
        const val MESSAGE_UNPAIRED_TRUCK = "There is no vehicle or vehicle In-Active"
        const val MSG_LOGIN_ON_ANOTHER_DEVICE = "Current device ID does not match given device ID"
    }
}
