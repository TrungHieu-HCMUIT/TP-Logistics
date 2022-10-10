package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.delivery_location

import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface DeliveryLocationUV : BaseUserView {
    fun updateLatestJob(latestJob: Job)
    fun showDischargeMaterial(jobStatus: ApiConst.JobStatus)
    fun confirmArriveAtDelivery()
    fun deliveryArriveDone(jobStatus: ApiConst.JobStatus)
}