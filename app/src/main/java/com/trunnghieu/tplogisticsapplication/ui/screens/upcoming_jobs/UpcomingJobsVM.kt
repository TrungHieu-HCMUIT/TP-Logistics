package com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs

import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_driver_report.DeliveryDriverReportRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

class UpcomingJobsVM : BaseRepoViewModel<DeliveryDriverReportRepo, UpcomingJobsUV>() {

    override fun createRepo(): DeliveryDriverReportRepo {
        return DeliveryDriverReportRepo()
    }

    val showRefreshButton = MutableLiveData(false)
    val jobSelectionAllowed = MutableLiveData(
        AppPreferences.get().getBoolean(AppPrefs.LOGIN.JOB_SELECTION_ALLOWED)
    )
    val enableStartWork = MutableLiveData(false)
    var selectedJob: Job? = null

    /**
     * Back to previous screen
     */
    fun backPress() {
        uiCallback?.onFragmentBackPressed()
    }

    /**
     * List assigned job
     */
    fun listAssignedJob() {
        val jobs = mutableListOf(
            //TODO: Fake data
            Job(
                bookingNo = "",
                loadNo = 1,
                showDetail = false,
                tripBase = false,
                jobStatus = ApiConst.JobStatus.DRIVER_JOB_STARTED.statusCode,
                pickUpLocation = "Pick up location",
                pickUpLatitude = 10.7898189,
                pickUpLongitude = 106.6414713,
                deliveryLocation = "Delivery location",
                dischargeLatitude = 10.8700142,
                dischargeLongitude = 106.8008654,
                radius = 5.0
            ),
        )
        uiCallback?.gotAssignedJobs(jobs, true)
    }

    /**
     * Start work with selected job
     */
    fun startWork() {
        if (selectedJob == null) return

        selectedJob!!.showDetail = true
        if (jobSelectionAllowed.value == false) {
            // Driver cannot select the job they want
            uiCallback?.goToNextJob(selectedJob!!)
            return
        }
    }
}
