package com.trunnghieu.tplogisticsapplication.data.preferences

object AppPrefs {

    object LOGIN {
        const val IS_LOGIN = "IS_LOGIN"
        const val REMEMBER_ME = "LOGIN_REMEMBER_ME"
        const val COUNTRY_CODE = "COUNTRY_CODE"
        const val PHONE_NUMBER = "LOGIN_PHONE_NUMBER"
        const val PASSWORD = "LOGIN_PASSWORD"
        const val DEVICE_ID = "LOGIN_DEVICE_ID"
        const val PERMISSION = "LOGIN_PERMISSION"
        const val ACCOUNT_PREFS = "ACCOUNT_PREFS"
        const val ALREADY_ACCEPT_TC = "ALREADY_ACCEPT_TC"
        const val VIEW_JOB_ASSIGNED = "VIEW_JOB_ASSIGNED"
        const val JOB_SELECTION_ALLOWED = "JOB_SELECTION_ALLOWED"
    }

    object FORGOT_PASSWORD {
        const val MOBILE_NO = "FORGOT_PASSWORD_MOBILE_NO"
    }

    object DRIVER {
        const val FULL_NAME = "DRIVER_FULL_NAME"
        const val MOBILE_NO = "DRIVER_MOBILE_NO"
        const val AVATAR_URL = "DRIVER_AVATAR_URL"
        const val VEHICLE_NUMBER = "DRIVER_VEHICLE_NUMBER"
        const val VEHICLE_IS_PAIRED = "VEHICLE_IS_PAIRED"
        const val IS_RESET_PASSWORD = "DRIVER_IS_RESET_PASSWORD"
        const val JOB_STATUS = "DRIVER_JOB_STATUS"
        const val BOOKING_NO = "DRIVER_BOOKING_NO"
        const val ORG_CODE = "DRIVER_ORG_CODE"
        const val LOAD_NO = "DRIVER_LOAD_NO"
        const val LAST_TIME_OPEN_APP = "LAST_TIME_OPEN_APP"
    }

    object JOB {
        const val LATEST_JOB = "LATEST_JOB"
    }

}