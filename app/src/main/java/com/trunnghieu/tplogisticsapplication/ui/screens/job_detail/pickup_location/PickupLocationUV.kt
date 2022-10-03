package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_location

import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface PickupLocationUV : BaseUserView {
    fun updateLatestJob(latestJob: Job)
    fun confirmArriveAtPickup()
    fun pickupArriveDone(jobStatus: ApiConst.JobStatus)
}