package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.driver.DriverRepo
import com.trunnghieu.tplogisticsapplication.databinding.FragmentJobDetailBinding
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.delivery_location.DeliveryLocationFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.discharge_material.DischargeMaterialFragment

class JobDetailFragment : BaseFragment<FragmentJobDetailBinding, JobDetailVM>(), JobDetailUV {

    override fun layoutRes() = R.layout.fragment_job_detail

    override fun viewModelClass(): Class<JobDetailVM> {
        return JobDetailVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: JobDetailVM) {
        viewModel.run {
            init(this@JobDetailFragment)
            binding.vm = this
        }
        binding.jobVM = jobVM
    }

    private var childRootFragment: Fragment? = null

    override fun initView() {

    }

    override fun initData() {
        viewModel.run {
            // Observer job status
            observerJobStatus(fragmentContext, this@JobDetailFragment)

            // Store current job no
            jobNo = jobVM.latestJob.value?.run {
                "$bookingNo-$loadNo"
            } ?: ""
        }
    }

    override fun initAction() {

    }

    override fun showDeliveryLocation() {
        //TODO: Add logic here
//        if (jobVM.latestJob.value?.hasDeliveryLocation == false) {
//            viewModel.jobTitle.value =
//                fragmentContext.getString(R.string.job_detail_title_discharge_material)
//            viewModel.replaceRootFragment(
//                DischargeMaterialFragment.newInstance(
//                    true,
//                    jobVM.latestJob.value?.landenWeight ?: 0.0,
//                    jobVM.latestJob.value?.actualWeightKg ?: 0.0
//                )
//            )
//        } else {
            viewModel.jobTitle.value =
                fragmentContext.getString(R.string.job_detail_title_to_delivery)
            viewModel.replaceRootFragment(DeliveryLocationFragment())
//        }
    }

    override fun showDischargeMaterial(showJobCompleteButton: Boolean) {
        viewModel.replaceRootFragment(
            DischargeMaterialFragment.newInstance(
                showJobCompleteButton,
                0.0,
                0.0
            )
        )
    }

    override fun replaceRootFragment(fragment: Fragment) {
        childRootFragment = fragment
        childFragmentManager
            .beginTransaction()
            .replace(binding.jobDetailContainerView.id, fragment)
            .commitNow()
    }

    fun getChildFragment(): Fragment? {
        return childRootFragment
    }

    fun getViewModel() = viewModel
}
