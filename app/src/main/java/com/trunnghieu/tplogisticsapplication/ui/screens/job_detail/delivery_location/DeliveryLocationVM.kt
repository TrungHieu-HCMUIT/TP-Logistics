package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.delivery_location

import android.location.Location
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.data.repository.remote.work_flow_service.WorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.ui.services.DriverLocationService
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger

class DeliveryLocationVM : BaseRepoViewModel<WorkFlowRepo, DeliveryLocationUV>() {

    override fun createRepo(): WorkFlowRepo {
        return WorkFlowRepo()
    }

    var isConnected = true
    private var hasSubmitDeliveryArrive = false
    private lateinit var latestJob: Job

    /**
     * Show local job data
     * In case no-connectivity
     */
    fun showLocalJob() {
//        val latestJob = LocalJob.get().getLatestJob() ?: return
//        uiCallback?.updateLatestJob(latestJob)
//        if (!latestJob.hasDeliveryLocation) {
//            uiCallback?.showDischargeMaterial(ApiConst.JobStatus.valueOf(latestJob.jobStatus))
//        }
    }

    /**
     * Get job data at delivery arrive
     */
    fun getJobAtDeliveryArrive() {
//        hasSubmitDeliveryArrive = false
//        repo?.getJobAtDeliveryArrive(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                uiCallback?.updateLatestJob(data)
//                if (!data.hasDeliveryLocation) {
//                    uiCallback?.showDischargeMaterial(ApiConst.JobStatus.valueOf(data.jobStatus))
//                }
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
        deliveryLat: Double,
        deliveryLng: Double,
        radius: Double
    ) {
        DriverLocationService.lastKnownLocationOfDriver = location
        val deliveryLatLng = Location("").apply {
            latitude = deliveryLat
            longitude = deliveryLng
        }
        val distance = location.distanceTo(deliveryLatLng)
        if (distance > radius) return
        if (hasSubmitDeliveryArrive) return
        hasSubmitDeliveryArrive = true
        CustomLogger.e("Driver is nearly Delivery Location -> Submit Delivery Arrive")
        submitDeliveryArrive()
    }

    /**
     * Action arrive at delivery
     */
    fun confirmArriveAtDelivery() {
        uiCallback?.confirmArriveAtDelivery()
    }

    /**
     * Submit delivery arrive status
     */
    fun submitDeliveryArrive() {
//        if (!isConnected) {
//            // No-Connectivity mode
//            latestJob = LocalJob.get().getLatestJob() ?: return
//            latestJob.jobStatus = ApiConst.JobStatus.DRIVER_DELIVERY_ARRIVED.statusCode
            uiCallback?.deliveryArriveDone(ApiConst.JobStatus.DRIVER_DELIVERY_ARRIVED)
//            return
//        }
//
//        repo?.submitDeliveryArrive(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                moveToDischargeMaterial(data)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun moveToDischargeMaterial(latestJob: Job) {
//        uiCallback?.run {
//            updateLatestJob(latestJob)
//            deliveryArriveDone(ApiConst.JobStatus.valueOf(latestJob.jobStatus))
//        }
    }
}
