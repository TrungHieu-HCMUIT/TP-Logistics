package com.trunnghieu.tplogisticsapplication.data.repository.remote.account.cso

import com.google.gson.annotations.SerializedName

data class CsoPhoneNumber(
    @SerializedName("phoneNo")
    val phoneNo: String,
    @SerializedName("supportName")
    val supportName: String
)