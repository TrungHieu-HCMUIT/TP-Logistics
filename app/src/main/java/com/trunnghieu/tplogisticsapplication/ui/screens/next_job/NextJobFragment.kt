package com.trunnghieu.tplogisticsapplication.ui.screens.next_job

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.local.driver.DriverRepo
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.FragmentNextJobBinding
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.JobDetailFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.maps.MapsForJobFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs.UpcomingJobsFragment

class NextJobFragment : MapsForJobFragment<FragmentNextJobBinding, NextJobVM>(), NextJobUV {

    companion object {
        const val ARG_NEW_JOB = "ARG_NEW_JOB"

        fun newInstance(newJob: Job? = LocalJob.get().getLatestJob()) = NextJobFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_NEW_JOB, newJob ?: LocalJob.get().getLatestJob())
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_next_job

    override fun viewModelClass(): Class<NextJobVM> {
        return NextJobVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: NextJobVM) {
        viewModel.init(this@NextJobFragment)
        binding.apply {
            vm = viewModel
            locationVM = this@NextJobFragment.locationVM
            jobVM = this@NextJobFragment.jobVM
        }
    }

    private lateinit var mapsBottomSheet: BottomSheetBehavior<View>

    override fun initMapsView() = binding.viewMaps.mapView

    override fun initView() {
        mapsBottomSheet = BottomSheetBehavior.from(binding.viewMaps.root)
        calculatePeekHeightForBottomSheet(binding.viewJobData) { peekHeight ->
            mapsBottomSheet.peekHeight = peekHeight
        }

        viewModel.getExtras(arguments)
    }

    override fun initAction() {
        super.initAction()
        binding.viewMaps.run {
            setExpandCollapseMapsOnClick {
                if (mapsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
                    mapExpanded = false
                    mapsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    mapExpanded = true
                    mapsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    override fun onFragmentBackPressed() {
        if (viewModel.needRefreshUpcomingJobs) {
            viewModel.needRefreshUpcomingJobs = false
            setFragmentResult(UpcomingJobsFragment.KEY_REFRESH_JOBS, Bundle())
        }
        super.onFragmentBackPressed()
    }

    override fun loadMapData() {
        locationVM.showDataOnMap(jobVM.latestJob.value)
    }

    override fun acceptJobSuccess(job: Job) {
        job.showDetail = true
        jobVM.latestJob.value = job

        // TODO: Delete here
        LocalJob.get().saveLatestJob(job)

        navigator.rootFragment = JobDetailFragment()
    }

    fun getViewModel() = viewModel
}