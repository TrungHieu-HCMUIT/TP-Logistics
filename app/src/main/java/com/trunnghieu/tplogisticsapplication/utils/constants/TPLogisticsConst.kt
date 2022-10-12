package com.trunnghieu.tplogisticsapplication.utils.constants

import android.os.Build
import com.trunnghieu.tplogisticsapplication.R

object TPLogisticsConst {

    const val TIMER_INTERVAL: Long = 1000

    const val SHOW_HIGHLIGHT_INTERVAL: Long = 30_000 // 30s

    val NOTIFICATION_ICON =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            R.drawable.ic_notification_from_android12
        else
            R.drawable.ic_notification_below_android12

    enum class AppLanguage(val code: String) {
        ENGLISH("en"),
        VIETNAMESE("vi")
    }

    object AppDateTime {
        const val SERVER_DATE = "yyyy-MM-dd HH:mm:ss.SSS"
        const val HISTORY_FILTER_DATE = "dd MMM yyyy"
        const val JOB_DATE = "dd/MM/yyyy"
        const val JOB_TIME = "HH:mm"
        const val STATEMENT_DATE = "yyyyMMdd"
        const val STATEMENT_LABEL = JOB_DATE
        const val EDO_ZIP = "ddMMyyyy"
    }

    object AppUnit {
        const val WEIGHT = "kg"
    }

    object FILE {
        const val EDO_FILE_NAME = "eDO_%s-%s.pdf"
        const val EDO_ZIP_FILE_NAME = "eDO_%s.zip"
        const val STATEMENT_FILE_NAME = "Statement_%s_%s_%s.pdf"
        const val IMAGE_MAX_SIZE = 1024
        val IMAGE_MIME_TYPES = arrayOf(
            "image/png",
            "image/jpg",
            "image/jpeg"
        )
    }

    object LOCATION {
        const val TRACKING_LOCATION_PERIOD = 15_000L
        const val REQUEST_LOCATION_SERVICE_INTERVAL = 10000L
        const val REQUEST_GOOGLE_LOCATION_SERVICE_INTERVAL = 10000L
        const val CAMERA_PADDING = 200
        const val CAMERA_ZOOM = 16f
    }

    object JOB {
        const val TIME_REFRESH_QR: Long = 3000
    }

    enum class ProductCategory(val value: String) {
        WORKING_TIME("WorkingTime")
    }

}