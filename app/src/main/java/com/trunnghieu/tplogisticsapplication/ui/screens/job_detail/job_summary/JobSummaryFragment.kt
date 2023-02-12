package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.job_summary

import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.FragmentJobSummaryBinding
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.end_work.EndWorkFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.next_job.NextJobFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.upcoming_jobs.UpcomingJobsFragment

class JobSummaryFragment : BaseFragment<FragmentJobSummaryBinding, JobSummaryVM>(), JobSummaryUV {

    override fun layoutRes() = R.layout.fragment_job_summary

    override fun viewModelClass(): Class<JobSummaryVM> {
        return JobSummaryVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: JobSummaryVM) {
        viewModel.init(this)
        binding.apply {
            vm = viewModel
            jobVM = this@JobSummaryFragment.jobVM
        }
    }

    override fun initView() {

    }

    override fun initData() {
        viewModel.getJobComplete()
    }

    override fun initAction() {

    }

    override fun updateLatestJob(latestJob: Job) {
        jobVM.latestJob.value = latestJob
    }

    override fun noJobIsAvailable(viewJobAssigned: Boolean) {
        showAlert(
            fragmentContext.getString(R.string.start_work_msg_no_job_available),
            if (viewJobAssigned)
                fragmentContext.getString(R.string.upcoming_jobs_btn_wait)
            else
                null,
            fragmentContext.getString(R.string.start_work_btn_end_work),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    if (viewJobAssigned) {
                        goToUpcomingJobs()
                    }
                }

                override fun onNegativeClick() {
                    viewModel.endWork()
                }
            }
        )
    }

    override fun goToNextJob(newJob: Job, jobStatus: ApiConst.JobStatus) {
        jobVM.changeJobStatus(jobStatus)
        navigator.goTo(NextJobFragment.newInstance(newJob))
    }

    override fun endWork() {
        navigator.rootFragment = EndWorkFragment()
    }

    override fun goToUpcomingJobs() {
        LocalJob.get().clearLatestJob()
        navigator.goTo(UpcomingJobsFragment())
    }
}
