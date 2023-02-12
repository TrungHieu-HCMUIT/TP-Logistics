package com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs

import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.FakeData
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
        // TODO: Fake data
        val jobs = FakeData.upcomingJobList
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
