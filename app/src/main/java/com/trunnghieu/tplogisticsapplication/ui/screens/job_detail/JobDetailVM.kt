package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.driver.DriverRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.DeliveryWorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_location.PickupLocationFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_material.PickupMaterialFragment

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
        DriverRepo.get().latestJobStatus.observe(lifecycleOwner) { jobStatus ->
            if (jobStatus == ApiConst.JobStatus.DRIVER_JOB_STARTED) {
                jobTitle.value = context.getString(R.string.job_detail_title_to_pickup)
                replaceRootFragment(PickupLocationFragment())
            }
            if (jobStatus == ApiConst.JobStatus.DRIVER_PICKUP_ARRIVED
            ) {
                jobTitle.value = context.getString(R.string.job_detail_title_pickup_material)
                replaceRootFragment(PickupMaterialFragment(this))
            }
            if (jobStatus == ApiConst.JobStatus.DRIVER_PICKUP_DONE) {
                uiCallback?.showDeliveryLocation()
            }
            if (jobStatus == ApiConst.JobStatus.DRIVER_DELIVERY_ARRIVED
                || jobStatus == ApiConst.JobStatus.DRIVER_DISCHARGED_DONE
            ) {
                jobTitle.value = context.getString(R.string.job_detail_title_discharge_material)
                uiCallback?.showDischargeMaterial(true)
            }
//            if (jobStatus == ApiConst.JobStatus.DRIVER_DISCHARGED) {
//                jobTitle.value = context.getString(R.string.job_detail_title_discharge_material)
//                uiCallback?.showDischargeMaterial(false)
//            }
        }
    }

    /**
     * Replace screen
     */
    fun replaceRootFragment(fragment: Fragment) {
        uiCallback?.replaceRootFragment(fragment)
    }
}
