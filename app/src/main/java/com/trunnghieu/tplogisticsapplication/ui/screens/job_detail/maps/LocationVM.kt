package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseUiViewModel

class LocationVM : BaseUiViewModel<LocationUV>() {

    // Maps
    private var locationManager: LocationManager? = null
    private var userLatLng = LatLng(0.0, 0.0)

    // Locations
    private var isPickupLocation = true
    private var pickupLatLng: LatLng? = null
    private var dischargeLatLng: LatLng? = null

    /**
     * Init location manager
     */
    fun initLocation(locationManager: LocationManager) {
        this.locationManager = locationManager
    }

    /**
     * Location has changed
     */
    fun onLocationChanged(location: Location) {
        userLatLng = LatLng(location.latitude, location.longitude)
    }

    /**
     * Show Google Maps with data
     */
    fun showDataOnMap(job: Job? = null) {
        job ?: return
        // TODO: Init data
        isPickupLocation = true
//        isPickupLocation = job.jobStatus == ApiConst.JobStatus.DRIVER_JOB_STARTED.name

        // Pickup
        val pickupName = "Pick up location"
//        val pickupName = job.pickUpLocation
        pickupLatLng = LatLng(10.7898189, 106.6414713)
//        pickupLatLng = LatLng(job.pickUpLatitude, job.pickUpLongitude)

        // Discharge
        val dischargeName = "Delivery location"
//        val dischargeName = job.deliveryLocation
        dischargeLatLng = LatLng(10.8700142, 106.8008654)
        uiCallback?.showDataOnMap(
            userLatLng,
            pickupName,
            pickupLatLng!!,
            dischargeName,
            dischargeLatLng!!
        )
    }

    /**
     * Move map to my location
     */
    @SuppressLint("MissingPermission")
    fun showMyLocation() {
        locationManager ?: return
        val lastKnownLocation =
            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocation ?: return
        uiCallback?.zoomCameraToLastKnownLocation(
            LatLng(
                lastKnownLocation.latitude,
                lastKnownLocation.longitude
            )
        )
    }

    /**
     * Open Google Maps & start navigation
     */
    @SuppressLint("MissingPermission")
    fun openAndStartNavigationOnMap() {
        locationManager ?: return
        val lastKnownLocation =
            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocation ?: return

        val uriBuilder = Uri.Builder()
            .apply {
                scheme("https")
                authority("www.google.com")
                appendPath("maps")
                appendPath("dir")
                appendPath("")
                appendQueryParameter("api", "1")
                appendQueryParameter(
                    "origin",
                    "${lastKnownLocation.latitude},${lastKnownLocation.longitude}"
                )
            }
        if (isPickupLocation) {
            uriBuilder.appendQueryParameter(
                "destination",
                "${pickupLatLng?.latitude},${pickupLatLng?.longitude}"
            )
        } else {
            uriBuilder.appendQueryParameter(
                "destination",
                "${dischargeLatLng?.latitude},${dischargeLatLng?.longitude}"
            )
        }
        val mapUrl = uriBuilder.toString()
        uiCallback?.openMap(mapUrl)
    }
}