package com.trunnghieu.tplogisticsapplication.ui.screens.next_job

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.data.repository.remote.work_flow_service.WorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel

class NextJobVM : BaseRepoViewModel<WorkFlowRepo, NextJobUV>() {

    override fun createRepo(): WorkFlowRepo {
        return WorkFlowRepo()
    }

    var needRefreshUpcomingJobs = false
    val newJob = MutableLiveData<Job>()
    var jobNo: String = ""

    /**
     * Get extras data from bundle
     */
    fun getExtras(argument: Bundle?) {

    }

    /**
     * Back to previous screen
     */
    fun backPress() {
        uiCallback?.onFragmentBackPressed()
    }

    /**
     * Submit accept job
     */
    fun submitAcceptJob() {

    }
}