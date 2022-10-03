package com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val bookingNo: String,
    var showDetail: Boolean = false,
    var tripBase: Boolean = false,
    @SerializedName("jobStatus")
    var jobStatus: Int = 3,
) : Parcelable {
}