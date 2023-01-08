package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_location

import android.location.Location
import android.util.Log
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.data.repository.remote.work_flow_service.WorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.ui.services.DriverLocationService
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger

class PickupLocationVM : BaseRepoViewModel<WorkFlowRepo, PickupLocationUV>() {

    override fun createRepo(): WorkFlowRepo {
        return WorkFlowRepo()
    }

    var isConnected = true
    private var hasSubmitPickupArrive = false
    private lateinit var latestJob: Job

    /**
     * Show local job data
     * In case no-connectivity
     */
    fun showLocalJob() {
        val latestJob = LocalJob.get().getLatestJob() ?: return
        uiCallback?.updateLatestJob(latestJob)
    }

    /**
     * Get job data at Pickup Arrive
     */
    fun getJobAtPickupArrive() {
        hasSubmitPickupArrive = false
//        repo?.getJobAtPickupArrive(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                uiCallback?.updateLatestJob(data)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    /**
     * Calculate location with radius to detect if driver is nearly at Pickup Location
     */
    fun calculateLocationWithRadius(
        location: Location,
        pickupLat: Double,
        pickupLng: Double,
        radius: Double
    ) {
        DriverLocationService.lastKnownLocationOfDriver = location
        val pickupLatLng = Location("").apply {
            latitude = pickupLat
            longitude = pickupLng
        }
        val distance = location.distanceTo(pickupLatLng)
        if (distance > radius) return
        if (hasSubmitPickupArrive) return
        hasSubmitPickupArrive = true
        CustomLogger.e("Driver is nearly Pickup Location -> Submit Pickup Arrive")
        submitPickupArrive()
    }

    /**
     * Action arrive at delivery
     */
    fun confirmArriveAtPickup() {
        uiCallback?.confirmArriveAtPickup()
    }

    /**
     * Submit pickup arrive status
     */
    fun submitPickupArrive() {
//        if (!isConnected) {
        // No-Connectivity Mode
        latestJob = LocalJob.get().getLatestJob() ?: return
        latestJob.jobStatus = ApiConst.JobStatus.DRIVER_PICKUP_ARRIVED.statusCode
        uiCallback?.pickupArriveDone(ApiConst.JobStatus.DRIVER_PICKUP_ARRIVED)
//            return
//        }

//        repo?.submitPickupArrive(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                moveToPickupMaterial(data)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun moveToPickupMaterial(latestJob: Job) {
        uiCallback?.run {
            updateLatestJob(latestJob)
            pickupArriveDone(ApiConst.JobStatus.fromInt(latestJob.jobStatus))
        }
    }
}
