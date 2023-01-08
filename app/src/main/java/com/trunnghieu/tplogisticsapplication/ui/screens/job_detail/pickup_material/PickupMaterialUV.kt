package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_material

import android.graphics.Bitmap
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface PickupMaterialUV : BaseUserView {
    fun jobIsNotAvailable()

    fun showDoReminder(isTripBased: Boolean, hasPickupDoReminder: Boolean, pickUpQRScanned: Boolean)

    fun showWeight(
        showWeightData: Boolean,
        showInputNetWeight: Boolean,
        showNetWeightHighlighted: Boolean,
        showEditNetWeight: Boolean
    )

    fun showBtnAction(showActionBtn: Boolean, actionForPickupDone: Boolean)

    fun resetWeight()
    fun confirmPickupDone(
        isTripBased: Boolean,
        doReminder: Boolean,
        eSignAvailable: Boolean,
        didTonnageSubmissionLocationIsPickup: Boolean
    )

    fun confirmNetWeight()
    fun updateLatestJob(latestJob: Job)
    fun showESignAtPickup()
    fun showWorkingScreen()
    fun pickupDone(jobStatus: ApiConst.JobStatus)
}
