package com.trunnghieu.tplogisticsapplication.ui.screens.job_history

import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_driver_report.DeliveryDriverReportRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.constants.TPLogisticsConst
import com.trunnghieu.tplogisticsapplication.utils.helper.DateTimeHelper

class HistoryVM : BaseRepoViewModel<DeliveryDriverReportRepo, HistoryUV>() {

    override fun createRepo(): DeliveryDriverReportRepo {
        return DeliveryDriverReportRepo()
    }

    val showFilter = MutableLiveData(false)
    val filterDate =
        MutableLiveData(DateTimeHelper.currentMillisToDate(TPLogisticsConst.AppDateTime.HISTORY_FILTER_DATE))
    var selectionDate = 0L
    val historyList = MutableLiveData<List<Job>>()

    /**
     * On back pressed
     */
    fun backPress() {
        uiCallback?.onFragmentBackPressed()
    }

    /**
     * Show / Hide filter
     */
    fun showFilter() {
        val show = !(showFilter.value ?: false)
        if (show) {
            uiCallback?.showDatePicker()
        }
        showFilter.value = show
    }

    /**
     * Show date picker
     */
    fun showDatePicker() {
        if (showFilter.value == false) return
        uiCallback?.showDatePicker()
    }

    /**
     * Request get all history
     */
    fun getAllHistoryJobs() {
        val jobs = mutableListOf(
            Job(bookingNo = ""),
            Job(bookingNo = ""),
            Job(bookingNo = ""),
            Job(bookingNo = ""),
        )
        historyList.value = jobs
    }

    /**
     * Preview and download all eDO files
     */
    fun downloadAllEdo() {

    }
}