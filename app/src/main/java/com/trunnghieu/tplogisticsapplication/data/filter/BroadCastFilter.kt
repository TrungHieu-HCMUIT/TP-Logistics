package com.trunnghieu.tplogisticsapplication.data.filter

object BroadCastFilter {

    const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    const val ACTION_ACCOUNT_CONFLICT = "com.trunnghieu.tplogisticsapplication.ACTION_ACCOUNT_CONFLICT"
    const val ACTION_REFRESH_DATA = "com.trunnghieu.tplogisticsapplication.ACTION_REFRESH_DATA"
    const val ACTION_JOB_CANCELLED = "com.trunnghieu.tplogisticsapplication.ACTION_JOB_CANCELLED"
    const val ACTION_UPDATE_AVAILABLE = "com.trunnghieu.tplogisticsapplication.ACTION_UPDATE_AVAILABLE"
    const val ACTION_UNPAIRED_TRUCK = "com.trunnghieu.tplogisticsapplication.ACTION_UNPAIRED_TRUCK"
    const val ACTION_PAIRED_TRUCK = "com.trunnghieu.tplogisticsapplication.ACTION_PAIRED_TRUCK"
    const val ACTION_JOB_UNASSIGNED = "com.trunnghieu.tplogisticsapplication.ACTION_JOB_UNASSIGNED"

    object Extras{
        const val EXTRA_VEHICLE_NO = "EXTRA_VEHICLE_NO"
        const val EXTRA_JOB_CHANGED = "EXTRA_JOB_CHANGED"
    }
}