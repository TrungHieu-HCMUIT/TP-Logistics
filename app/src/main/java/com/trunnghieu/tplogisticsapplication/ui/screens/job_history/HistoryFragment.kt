package com.trunnghieu.tplogisticsapplication.ui.screens.job_history

import android.Manifest
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.FragmentHistoryBinding
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.DateTimePicker
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job_history.adapter.HistoryAdapter
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.utils.helper.DateTimeHelper

class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryVM>(), HistoryUV,
    BaseItemClickListener<Job> {

    override fun layoutRes() = R.layout.fragment_history

    override fun viewModelClass(): Class<HistoryVM> {
        return HistoryVM::class.java
    }

    override fun initViewModel(viewModel: HistoryVM) {
        viewModel.run {
            init(this@HistoryFragment)
            binding.vm = this
        }
    }

    // Adapter
    private val historyAdapter = HistoryAdapter(this)

    override fun initView() {

    }

    override fun initData() {
        binding.rvHistory.adapter = historyAdapter

        viewModel.run {
            getAllHistoryJobs()

            // Observer history
            historyList.observe(this@HistoryFragment) { _historyList ->
                historyAdapter.run {
                    submitList(null)
                    submitList(_historyList)
                }
            }
        }
    }

    override fun initAction() {

    }

    override fun showDatePicker() {
        DateTimePicker.createDatePicker(
            limitToCurrentDate = true,
            limitToStart = false,
            selection = viewModel.selectionDate,
            theme = R.style.TPLogistics_DatePicker
        ).run {
            addOnPositiveButtonClickListener { milliseconds ->
                val selectedDate = DateTimeHelper.millisToDate(
                    milliseconds,
                    TPLogisticsConst.AppDateTime.HISTORY_FILTER_DATE
                )
                viewModel.run {
                    filterDate.value = selectedDate
                    selectionDate = milliseconds
                    getAllHistoryJobs()
                }
            }
            show(navigator.activeFragment!!.childFragmentManager, tag)
        }
    }

    override fun onItemClick(item: Job) {
        requestPermission(
            fragmentContext.getString(R.string.permission_msg),
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), object : BaseActivity.RequestPermissionCallback {
                override fun onPermissionGranted() {
                    // TODO: Navigate to DownloadFragment
                }

                override fun onPermissionDenied() {

                }
            }
        )
    }

//    override fun previewAndDownloadAllEdo(histories: List<Job>) {
//        navigator.goTo(
//            DownloadFragment.newInstance(
//                histories.size == 1,
//                true,
//                ArrayList(histories),
//                null
//            )
//        )
//    }
}