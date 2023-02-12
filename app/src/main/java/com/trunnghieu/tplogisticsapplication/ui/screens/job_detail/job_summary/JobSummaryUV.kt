package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.job_summary

import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface JobSummaryUV : BaseUserView {
    fun updateLatestJob(latestJob: Job)
    fun noJobIsAvailable(viewJobAssigned: Boolean)
    fun goToNextJob(newJob: Job, jobStatus: ApiConst.JobStatus)
    fun endWork()
    fun goToUpcomingJobs()
}
