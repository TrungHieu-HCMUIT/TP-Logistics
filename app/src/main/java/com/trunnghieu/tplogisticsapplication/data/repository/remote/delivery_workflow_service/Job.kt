package com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val bookingNo: String,
    var showDetail: Boolean,
    val loadNo: Int,
    var tripBase: Boolean,
    @SerializedName("jobStatus")
    var jobStatus: Int,
    @SerializedName("pickUpLocation")
    var pickUpLocation: String,
    @SerializedName("pickUpLatitude")
    val pickUpLatitude: Double,
    @SerializedName("pickUpLongitude")
    val pickUpLongitude: Double,
    @SerializedName("deliveryLocation")
    var deliveryLocation: String,
    @SerializedName("dischargeLatitude")
    val dischargeLatitude: Double,
    @SerializedName("dischargeLongitude")
    val dischargeLongitude: Double,
    @SerializedName("radius")
    val radius: Double,
) : Parcelable {
}
