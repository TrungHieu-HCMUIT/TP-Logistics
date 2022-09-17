package com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs

import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface UpcomingJobsUV : BaseUserView {
    fun onFragmentBackPressed()
    fun listAssignedJobsIsEmpty()
    fun gotAssignedJobs(jobList: List<Job>, jobSelectionAllowed: Boolean)
    fun goToNextJob(selectedJob: Job)
}