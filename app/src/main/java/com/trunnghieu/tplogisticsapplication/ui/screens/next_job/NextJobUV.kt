package com.trunnghieu.tplogisticsapplication.ui.screens.next_job

import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView


interface NextJobUV : BaseUserView {
    fun onFragmentBackPressed()
    fun acceptJobSuccess(job: Job)
}