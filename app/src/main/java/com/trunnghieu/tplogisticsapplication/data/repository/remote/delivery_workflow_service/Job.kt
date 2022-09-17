package com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val bookingNo: String,
) : Parcelable {
}