package com.trunnghieu.tplogisticsapplication.data.api

import com.trunnghieu.tplogisticsapplication.utils.constants.Const

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
    enum class JobStatus(code: Int) {
        OPEN(1),
        ASSIGNED(2),
        DRIVER_JOB_STARTED(3),
        DRIVER_PICKUP_ARRIVED(4),
        DRIVER_PICKUP_DONE(5),
        DRIVER_DELIVERY_STARTED(6),
        DRIVER_DELIVERY_ARRIVED(7),
        DRIVER_DISCHARGED_DONE(8),
        DRIVER_JOB_COMPLETED(9),
        CUSTOMER_CANCELLED(10);

        val statusCode: Int

        companion object {
            fun fromInt(value: Int): JobStatus? {
                values().forEach {
                    if (it.statusCode == value) {
                        return it
                    }
                }
                return null
            }
        }

        init {
            statusCode = code
        }
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
