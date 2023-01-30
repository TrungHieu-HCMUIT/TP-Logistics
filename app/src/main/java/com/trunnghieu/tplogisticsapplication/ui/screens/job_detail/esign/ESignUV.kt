package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.esign

import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface ESignUV : BaseUserView {
    fun clearSignature()
    fun showProgress(show: Boolean)
    fun progressUpdate(progress: Int)
    fun updateLatestJob(latestJob: Job)
    fun uploadESignSuccess()
    fun cancelESign()
    fun enableConfirm(enable: Boolean)
}
