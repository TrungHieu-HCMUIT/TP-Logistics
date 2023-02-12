package com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.trunnghieu.tplogisticsapplication.utils.helper.DateTimeHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val bookingNo: String,
    var showDetail: Boolean,
    val loadNo: Int,
    var tripBase: Boolean,
    @SerializedName("jobStatus")
    var jobStatus: Int,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("product")
    val product: String,
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
    @SerializedName("pickUpDoneTime")
    val pickUpDoneTime: String,
    @SerializedName("dischargeTime")
    val dischargeTime: String,
) : Parcelable {

    //TODO: Delete this method
    fun getFormattedDate(orgTime: String? = ""): String {
        return DateTimeHelper.currentMillisToDate("dd/MM/yyyy")
    }

    //TODO: Delete this method
    fun getFormattedTime(orgTime: String? = ""): String {
//        return DateTimeHelper.currentMillisToTime("HH:mm")
        return if (orgTime == "Discharge") "15:45" else "15:44"
    }
}
