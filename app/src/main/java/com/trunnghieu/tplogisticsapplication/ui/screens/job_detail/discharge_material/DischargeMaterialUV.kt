package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.discharge_material

import android.graphics.Bitmap
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface DischargeMaterialUV : BaseUserView {
    fun jobIsNotAvailable()

    fun showDoReminder(isTripBased: Boolean, hasPickupDoReminder: Boolean)

    fun showWeight(
        showWeightData: Boolean,
        showInputNetWeight: Boolean,
        showNetWeightHighlighted: Boolean,
        showEditNetWeight: Boolean
    )

    fun showLadenAndNetWeightFromPickup(ladenWeightFromPickup: Double, netWeightFromPickup: Double)

    fun showBtnAction(
        showSubmitBtn: Boolean,
        showActionBtn: Boolean,
        showDischargeBtn: Boolean,
        actionForJobComplete: Boolean
    )

    fun confirmNetWeight()
    fun resetWeight()
    fun showConfirmJob(isTripBased: Boolean, doReminder: Boolean, eSignAvailable: Boolean)
    fun startScanDO()
    fun updateLatestJob(latestJob: Job)
    fun dischargeDone(jobStatus: ApiConst.JobStatus)
    fun showESignAtDischarge()
    fun showWorkingTimeAtDischarge()
}
