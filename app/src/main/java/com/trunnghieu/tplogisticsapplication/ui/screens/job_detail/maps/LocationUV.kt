package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps

import com.google.android.gms.maps.model.LatLng
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface LocationUV : BaseUserView {
    fun showDataOnMap(
        userLatLng: LatLng,
        pickupName: String,
        pickupLatLng: LatLng,
        dischargeName: String,
        dischargeLatLng: LatLng
    )

    fun zoomCameraToLastKnownLocation(lastKnownLocation: LatLng)
    fun openMap(mapUrl: String)
}