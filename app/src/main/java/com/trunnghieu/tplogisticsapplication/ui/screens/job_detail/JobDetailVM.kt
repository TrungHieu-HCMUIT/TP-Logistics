package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.DeliveryWorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel

class JobDetailVM : BaseRepoViewModel<DeliveryWorkFlowRepo, JobDetailUV>() {

    override fun createRepo(): DeliveryWorkFlowRepo {
        return DeliveryWorkFlowRepo()
    }

    val jobTitle = MutableLiveData<String>()
    var jobNo: String = ""

    /**
     * Display job detail based on job status
     */
    fun observerJobStatus(context: Context, lifecycleOwner: LifecycleOwner) {

    }

    /**
     * Replace screen
     */
    fun replaceRootFragment(fragment: Fragment) {
        uiCallback?.replaceRootFragment(fragment)
    }
}